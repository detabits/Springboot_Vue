
<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入价目表名称" suffix-icon="el-icon-search" v-model="pricelistname"></el-input>
      <el-input style="width: 200px" placeholder="请输入价目表类型" suffix-icon="el-icon-search" class="ml-5" v-model="pricelisttype"></el-input>

      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>

      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          cancel-button-type=""
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="85" sortable></el-table-column>
      <el-table-column prop="pricelistname" label="价目表名称" width="85"></el-table-column>
      <el-table-column prop="area" label="提货厂区" width="85"></el-table-column>
      <el-table-column prop="pricelisttype" label="价目表类型" width="85"></el-table-column>
      <el-table-column prop="discount" label="折扣" width="80"></el-table-column>
      <el-table-column prop="pricelistdescription" label="价目表说明" width="85"></el-table-column>
      <el-table-column prop="effectivedate" label="生效日期" width="85"></el-table-column>
      <el-table-column prop="expirydate" label="失效日期" width="85"></el-table-column>






      <el-table-column label="操作"  width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
          cancel-button-type=""
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="产品价目表" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="85px" size="small">

        <el-form-item label="价目表名称">
          <el-input v-model="form.pricelistname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="提货厂区" label-width="150px">
          <el-select v-model="form.area" placeholder="请选择" style="width: 80%">
            <el-option v-for="item in optionsTH" :key="item.id" :label="item.itemid" :value="item.itemname"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价目表类型">
          <el-input v-model="form.pricelisttype" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="折扣">
          <el-input v-model="form.discount" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价目表说明">
          <el-input v-model="form.pricelistdescription" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="生效日期">
          <el-input v-model="form.effectivedate" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="失效日期">
          <el-input v-model="form.expirydate" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>


<script>



import API from "@/utils/request";

export default {
  name: "Productpricelist",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 2,
      pricelistname: "",
      pricelisttype: "",
      address: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      roles:[],
      optionsTH: [],
    }
  },

  created() {

    this.load()
  },

  methods: {
    load() {

      this.request.get("/productpricelist/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          pricelistname: this.pricelistname,
          pricelisttype: this.pricelisttype,
          address: this.address,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.records
        this.total = res.total
      })
      this.request.get("/role").then(res => {
        this.roles = res.data
      })
      API.get("/code").then(res => {
        this.optionsTH = res.data
      })
    },
    save() {
      this.request.post("/productpricelist", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/productpricelist/" + id).then(res => {
        if (res) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/productpricelist/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.pricelistname = ""
      this.pricelisttype = ""
      this.address = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },

    exp() {
      window.open("http://localhost:9090/productpricelist/export")
    },

    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>