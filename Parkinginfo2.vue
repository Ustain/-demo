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
          <el-table-column prop="小区" label="小区"/>
          <el-table-column prop="state" label="状态"/>
          <el-table-column prop="sid" label="车位号" width="180"/>
          <el-table-column prop="可用时间段" label="可用时间段"/>
          <el-table-column prop="评分" label="评分"/>
          <el-table-column prop="price" label="价格"/>
          <el-table-column prop="tel" label="电话号码"/>
          <el-table-column  label="图片">
            <template #default="scope">
              <el-image
                  style="width: 100px; height: 100px"
                  :src="scope.row.img"
                  :zoom-rate="1.2"
                  :preview-src-list="[scope.row.img]"
                  preview-teleported="true"
                  :initial-index="4"
                  fit="cover"
              />
            </template>
          </el-table-column>

          <el-table-column fixed="right" label="Operations" width="150px">
            <template #default="scope">
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>

              <el-popconfirm title="Are you sure to delete this?" @confirm="handleDelete(scope.row.id)" >
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
        <el-form :model="form" label-width="120px">
          <el-form-item label="小区">
            <el-input v-model="form.小区" style="width:80%"/>
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
          <el-form-item label="车位号">
            <el-input v-model="form.sid" style="width:80%"/>
          </el-form-item>
          <el-form-item label="可用时间段">
            <el-input v-model="form.可用时间段" style="width:80%"/>
          </el-form-item>
          <el-form-item label="评分">
            <el-input v-model="form.评分" style="width:80%"/>
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model="form.price" style="width:80%"/>
          </el-form-item>
          <el-form-item label="电话号码">
            <el-input v-model="form.tel" style="width:80%"/>
          </el-form-item>
          <el-form-item label="图片">
            <el-upload
                ref="upload"
                action="http://localhost:9092/files/upload "
                :on-success="filesUploadSuccess"
            >

              <el-button type="primary">点击上传</el-button>
            </el-upload>

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
  name: 'Parkinginfo',
  components: {},
  data() {
    return {
      form: {},
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
      request.get("/api/parkinginfo", {
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
      this.$nextTick(()=>{
        if (this.$refs['upload']){
          this.$refs['upload'].clearFiles();//清楚历史图片
        }
      });
    },

    save(){
      if (this.form.id){//更新
        request.put("/api/parkinginfo",this.form).then(res =>{
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
        request.post("/api/parkinginfo",this.form).then(res =>{
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
      request.delete("/api/parkinginfo/"+id).then(res => {
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
    filesUploadSuccess(res){
      console.log(res);
      this.form.img = res.data;
      this.load();
    }
  }
}
</script>
