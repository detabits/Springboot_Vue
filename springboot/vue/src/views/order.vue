<template>
  <div>
    <div style="margin: 10px 0">

      <el-input style="width: 200px" placeholder="请输入订单编号" suffix-icon="el-icon-search" class="ml-5" v-model="orderNo"></el-input>
      <el-input style="width: 200px" placeholder="请输入业务员" suffix-icon="el-icon-search" class="ml-5" v-model="salesman"></el-input>
      <el-input style="width: 200px" placeholder="请输入产品名称" suffix-icon="el-icon-search" class="ml-5" v-model="productname"></el-input>

      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
    </div>




    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="100" sortable> </el-table-column>
      <el-table-column prop="orderNo" label="订单编号"></el-table-column>
      <el-table-column prop="totalPrice" label="总价"></el-table-column>
      <el-table-column prop="state" label="状态"></el-table-column>
      <el-table-column prop="ordertype" label="订单类型"></el-table-column>
      <el-table-column prop="source" label="订单来源"></el-table-column>
      <el-table-column prop="salesman" label="业务员"></el-table-column>
      <el-table-column prop="customername" label="客户名称"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="productname" label="产品名称"></el-table-column>
      <el-table-column prop="quantity" label="数量"></el-table-column>
      <el-table-column prop="unit" label="提货单位"></el-table-column>
      <el-table-column prop="area" label="提货厂区"></el-table-column>
      <el-table-column prop="delivery" label="提货方式"></el-table-column>
      <el-table-column prop="linkAddress" label="送货地址"></el-table-column>
      <el-table-column prop="linkUser" label="联系人"></el-table-column>
      <el-table-column prop="linkPhone" label="联系电话"></el-table-column>
      <el-table-column prop="submission" label="提交日期"></el-table-column>
      <el-table-column prop="deadline" label="提交截止日期"></el-table-column>
      <el-table-column prop="notes" label="备注"></el-table-column>

      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button type="primary" @click="out(scope.row)" v-if="scope.row.state === '待发货'">发货</el-button>

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
      <el-form :model="entity">
        <el-form-item label="订单类型" label-width="150px">
          <el-input v-model="entity.ordertype" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="业务员" label-width="150px">
          <el-input v-model="entity.salesman" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="客户名称" label-width="150px">
          <el-input v-model="entity.customername" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="产品名称" label-width="150px">
          <el-select v-model="entity.productname" placeholder="请选择" style="width: 80%">
            <el-option v-for="item in optionsJC" :key="item.id" :label="item.productname" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量" label-width="150px">
          <el-input v-model="entity.quantity" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="提货方式" label-width="150px">
          <el-input v-model="entity.delivery" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="送货地址" label-width="150px">
          <el-input v-model="entity.linkAddress" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="联系人" label-width="150px">
          <el-input v-model="entity.linkUser" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" label-width="150px">
          <el-input v-model="entity.linkPhone" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="提交日期" label-width="150px">
          <el-date-picker style="width: 80%" v-model="entity.submission" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="提交截止日期" label-width="150px">
          <el-date-picker style="width: 80%" v-model="entity.deadline" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="备注" label-width="150px">
          <el-input type="textarea" v-model="entity.notes" autocomplete="off" style="width: 80%"></el-input>
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
const url = "/api/order/"

export default {
  name: "Order",
  data() {
    return {
      fileList: [],
      options: [],

      user: {},
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      entity: {},
      total: 0,
      dialogFormVisible: false,
      optionsJC: [],
      orderNo: "",
      salesman: "",
      productname: "",
    };
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    this.load()
  },
  methods: {
    out(obj) {
      this.entity = JSON.parse(JSON.stringify(obj))
      this.entity.state = '待收货'
      this.save()

    },
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
            orderNo: this.orderNo,
            salesman: this.salesman,
            productname: this.productname,
          }
       }).then(res => {
          this.tableData = res.data.records || []
          this.total = res.data.total
       })

      API.get("/product").then(res => {
        this.optionsJC = res.data
      })
    },
    reset() {
      this.orderNo = ""
      this.salesman = ""
      this.productname = ""

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
    handleAdd() {
      this.entity = {}
      this.fileList = []
      this.dialogFormVisible = true


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
