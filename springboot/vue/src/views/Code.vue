
<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入快码名称" suffix-icon="el-icon-search" class="ml-5" v-model="codename"></el-input>
      <el-input style="width: 200px" placeholder="请输入快码项名称" suffix-icon="el-icon-search" class="ml-5" v-model="itemname"></el-input>

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

    <el-table   :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="codeid" label="快码编码" width="80"></el-table-column>
      <el-table-column prop="codename" label="快码名称" width="140"></el-table-column>
      <el-table-column prop="itemid" label="快码项编码" width="120"></el-table-column>
      <el-table-column prop="itemname" label="快码项名称" width="120"></el-table-column>

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

    <el-dialog title="快码信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="85px" size="small">
        <el-form-item label="快码编码">
          <el-input v-model="form.codeid" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="快码名称">
          <el-input v-model="form.codename" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="快码项编码">
          <el-input v-model="form.itemid" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="快码项名称">
          <el-input v-model="form.itemname" autocomplete="off"></el-input>
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



export default {
  name: "Code",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 2,
      codename: "",
      itemname: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      roles:[]
    }
  },

  created() {

    this.load()
  },

  methods: {
    load() {

      this.request.get("/code/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          codename: this.codename,
          itemname: this.itemname
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.records
        this.total = res.total
      })
      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    save() {
      this.request.post("/code", this.form).then(res => {
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
      this.request.delete("/code/" + id).then(res => {
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
      this.request.post("/code/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.codename = ""
      this.itemname = ""
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
      window.open("http://localhost:9090/code/export")
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