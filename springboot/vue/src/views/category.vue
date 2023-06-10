<template>
  <div>
    <div style="padding: 5px 0">
      <el-input style="width: 200px" placeholder="请输入分类名称" suffix-icon="el-icon-search" v-model="name"></el-input>


      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>

      <el-button type="primary" @click="add">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
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


    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="55" sortable> </el-table-column>
      <el-table-column prop="name" label="分类名称" width="80"></el-table-column>
      <el-table-column prop="no" label="分类编号"  width="80"></el-table-column>
      <el-table-column prop="level" label="层级" width="80"></el-table-column>
      <el-table-column prop="leveldescription" label="层级描述" width="80"></el-table-column>
      <el-table-column prop="effectivedate" label="生效日期" width="80"></el-table-column>
      <el-table-column prop="expirydate" label="失效日期" width="80"></el-table-column>

      <el-table-column
          align="center"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" circle  @click="edit(scope.row)"></el-button>
          <el-popconfirm
              @confirm="del(scope.row.id)"
              title="确定删除？"
          >
            <el-button type="danger" icon="el-icon-delete" circle slot="reference" style="margin-left: 10px"></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 10px">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :page-sizes="[2, 5, 10]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>

    <!-- 弹窗   -->
    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="30%"
               :close-on-click-modal="false">
      <el-form :model="entity"  label-width="80px" size="small">
        <el-form-item label="分类名称" >
          <el-input v-model="entity.name" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="分类编号" >
          <el-input v-model="entity.no" autocomplete="off" ></el-input>
        </el-form-item>

        <el-form-item label="层级">
          <el-input v-model="entity.level" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="层级描述">
          <el-input v-model="entity.leveldescription" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="生效日期">
          <el-input v-model="entity.effectivedate" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="失效日期">
          <el-input v-model="entity.expirydate" autocomplete="off"></el-input>
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
import API from '../utils/request'
const url = "/api/category/"

export default {
  name: "Category",
  data() {
    return {
      fileList: [],
      options: [],
      text: '',
      user: {},
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      name:"",
      entity: {},
      total: 0,
      dialogFormVisible: false
    };
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    this.$emit('user', this.user);
    this.load()
  },
  methods: {
    fileSuccessUpload(res) {
      this.entity.file = "http://localhost:9090/static/file/" + res.data;
      this.fileList = [res.data]
      console.log(res)
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    load() {
       API.get(url + "/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.name
            //name: this.text
          }
       }).then(res => {
          this.tableData = res.data.records || []
          this.total = res.data.total
       })
    },
    reset() {
      this.name = ""
      this.load()
    },
    add() {
      this.entity = {}
      this.fileList = []
      this.dialogFormVisible = true
    },
    edit(obj) {
      this.entity = JSON.parse(JSON.stringify(obj))
      this.fileList = []
      this.dialogFormVisible = true
    },
    save() {
      if (!this.entity.id) {
        API.post(url, this.entity).then(res => {
           if (res.code === '200') {
             this.$message({
               type: "success",
               message: "操作成功"
             })
           } else {
             this.$message({
               type: "error",
               message: res.msg
             })
           }
           this.load()
           this.dialogFormVisible = false
        })
      } else {
        API.put(url, this.entity).then(res => {
          if (res.code === '200') {
            this.$message({
              type: "success",
              message: "操作成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load()
          this.dialogFormVisible = false
        })
      }
    },
    del(id) {
      API.delete(url + id).then(res => {
        this.$message({
          type: "success",
          message: "操作成功"
        })
        this.load()
      })
    }
  },
};
</script>

<style scoped>
</style>
