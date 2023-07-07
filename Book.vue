<template>
  <nav style="padding: 10px">
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-input prefix-icon="search" v-model="search" placeholder="请输入内容" style="width: 20%" size="mini" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>

    <div style="margin: 10px 0">
      <div v-if="tableData.length > 0">
        <el-table
            :data="tableData"
            border
            style="width: 100%">
          <el-table-column prop="id" label="ID" width="180" sortable/>
          //删掉width就自适应
          <el-table-column prop="sid" label="车位号" width="180"/>
          <el-table-column prop="小区" label="小区"/>
          <el-table-column prop="customerName" label="姓名"/>
          <el-table-column prop="tel" label="电话号码"/>
          <el-table-column prop="date" label="日期"/>
          <el-table-column prop="begintime" label="开始时间"/>
          <el-table-column prop="endtime" label="结束时间"/>
          <el-table-column prop="state" label="状态"/>

          <el-table-column fixed="right" label="Operations" width="150px">
            <template #default="scope">
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>

              <el-popconfirm title="确认删除吗?" @confirm="handleDelete(scope.row.id)" >
                <template #reference>
                  <el-button size="small" type="danger" >删除</el-button>
                </template>
              </el-popconfirm>

            </template>
          </el-table-column>

        </el-table>
      </div>
      <div v-else>
        数据加载中...
      </div>
    </div>
    <div style="margin: 10px 0">
      <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>

      <el-dialog v-model="dialogVisible" title="Tips" width="30%">
        <el-form :model="form" :rules="formRules" ref="form" label-width="120px">
          <el-form-item label="车位号">
            <el-input v-model="form.sid" style="width:80%"/>
          </el-form-item>
          <el-form-item label="小区">
            <el-input v-model="form.小区" style="width:80%"/>
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="form.customerName" style="width:80%"/>
          </el-form-item>
          <el-form-item label="电话号码">
            <el-input v-model="form.tel" style="width:80%"/>
          </el-form-item>

          <el-form-item label="日期" style="width:100%">
            <el-date-picker
                v-model="form.date"
                type="date"
                placeholder="Pick a Date"
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
            />
          </el-form-item>


          <el-form-item label="开始时间">
            <el-time-select
                v-model="form.begintime"
                :max-time="endtime"
                class="mr-4"
                placeholder="Start time"
                start="08:30"
                step="00:15"
                end="18:30"
                 clearable/>
          </el-form-item>

          <el-form-item label="结束时间">
            <el-time-select
                v-model="form.endtime"
                :min-time="form.begintime"
                placeholder="End time"
                start="08:30"
                step="00:15"
                end="18:30"
                clearable/>
          </el-form-item>

          <el-form-item label="状态">
            <div class="mb-2 flex items-center text-sm">
              <el-radio-group v-model="form.state" class="ml-4">
                <el-radio label="已预定">已预定</el-radio>
                <el-radio label="空闲中">空闲中</el-radio>
                <el-radio label="使用中">使用中</el-radio>
              </el-radio-group>
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">
          确 认
        </el-button>
      </span>
        </template>
      </el-dialog>
    </div>
  </nav>
</template>

<script>
// @ is an alias to /src
//import HelloWorld from '@/components/HelloWorld.vue'

import request from "@/utils/request";

export default {
  name: 'Book',
  components: {},
  data() {
    return {
      form: {
        sid: '',
        小区: '',
        customerName: '',
        tel: '',
        date: null,
        begintime: null,
        endtime: null,
        state: ''
      },
      formRules: {
        sid: [{ required: true, message: '请输入车位号', trigger: 'blur' }],
        小区: [{ required: true, message: '请输入小区', trigger: 'blur' }],
        customerName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        tel: [{ required: true, message: '请输入电话号码', trigger: 'blur' }],
        date: [{ required: true, message: '请选择日期', trigger: 'change' }],
        begintime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endtime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
        state: [{ required: true, message: '请选择状态', trigger: 'change' }]
      },
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize:10,
      total: 10,
      // pageNum:0,
      tableData: [
      ]
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      request.get("/api/book", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        console.log(res);
        this.tableData = res.data.records;
        this.total = res.data.total;
      })
    },

    add() {
      this.dialogVisible = true;
      this.form = {};
    },

    save(){
      if (this.form.id){//更新
        request.put("/api/book",this.form).then(res =>{
          console.log(res);
          if (res.code==='0'){
            this.$message({
              type: "success",
              message: "更新成功"
            })
          }else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load();//刷新表格数据
          this.dialogVisible = false;//关闭弹窗
        });
      }else {//新增
        request.post("/api/book",this.form).then(res =>{
          console.log(res);
          if (res.code==='0'){
            this.$message({
              type: "success",
              message: "新增成功"
            })
          }else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load();//刷新表格数据
          this.dialogVisible = false;//关闭弹窗
        });
      }

    },
    handleEdit(row) {
      this.form=JSON.parse(JSON.stringify(row));
      this.dialogVisible=true;
    },
    handleDelete(id){
      console.log(id);
      request.delete("/api/book/"+id).then(res => {
        if (res.code==='0'){
          this.$message({
            type: "success",
            message: "删除成功"
          })
        }else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()//重新加载
      });
    },
    handleSizeChange(pageSize){//改变当前页面个数
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum){//改变当前页码
      this.currentPage = pageNum;
      this.load();
    },
    // formateDate(datetime) {
    //   // let  = "2019-11-06T16:00:00.000Z"
    //   function addDateZero(num) {
    //     return (num < 10 ? "0" + num : num);
    //   }
    //   let d = new Date(datetime);
    //   let formatdatetime = d.getFullYear() + '-' + addDateZero(d.getMonth() + 1) + '-' + addDateZero(d.getDate());
    //   return formatdatetime;
    // },

  }
}
</script>
