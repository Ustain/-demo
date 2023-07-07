<!--<template>-->
<!--  <nav style="padding: 10px">-->
<!--&lt;!&ndash;    <div style="margin: 10px 0">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-button type="primary" @click="add">新增</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-button type="primary">导入</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-button type="primary">导出</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->


<!--        <el-form :model="form" label-width="120px" style="width: 40%" size="mini" clearable>-->

<!--          <el-form-item >-->
<!--            <H3>发布车位</H3>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="小区">-->
<!--            <el-input v-model="form.小区" style="width:80%"/>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="车位号">-->
<!--            <el-input v-model="form.sid" style="width:80%"/>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="可用时间段">-->
<!--            <el-input v-model="form.可用时间段" style="width:80%"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="价格">-->
<!--            <el-input v-model="form.price" style="width:80%"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="电话号码">-->
<!--            <el-input v-model="form.tel" style="width:80%"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item label="图片">-->
<!--            <el-upload ref="upload" action="http://localhost:9092/files/upload" :on-success="filesUploadSuccess">-->
<!--              <el-button type="primary">点击上传</el-button>-->
<!--            </el-upload>-->
<!--          </el-form-item>-->

<!--          <el-form-item >-->
<!--            <el-button type="primary" @click="add">发  布</el-button>-->
<!--            <el-button @click="handleEdit">修改发布</el-button>-->
<!--          </el-form-item>-->

<!--        </el-form>-->


<!--    <el-dialog v-model="dialogVisible" title="Tips" width="30%">-->
<!--      <el-form :model="form" label-width="120px">-->
<!--        <span>确认发布吗？</span>-->
<!--      </el-form>-->
<!--      <template #footer>-->
<!--      <span class="dialog-footer">-->
<!--        <el-button @click="dialogVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="save">-->
<!--          确认发布-->
<!--        </el-button>-->
<!--      </span>-->
<!--      </template>-->
<!--    </el-dialog>-->
<!--  </nav>-->
<!--</template>-->

<!--<script>-->
<!--// @ is an alias to /src-->
<!--//import HelloWorld from '@/components/HelloWorld.vue'-->

<!--import request from "@/utils/request";-->

<!--export default {-->
<!--  name: 'Shareparking',-->
<!--  components: {},-->
<!--  data() {-->
<!--    return {-->
<!--      form: {},-->
<!--      dialogVisible: false,-->
<!--      tableData: [-->
<!--      ]-->
<!--    }-->
<!--  },-->

<!--  methods: {-->

<!--    add() {-->
<!--      this.dialogVisible = true;-->
<!--      this.form = {};-->

<!--      this.$nextTick(()=>{-->
<!--        if (this.$refs['upload']){-->
<!--          this.$refs['upload'].clearFiles();//清楚历史图片-->
<!--        }-->
<!--      });-->
<!--    },-->

<!--    save(){-->
<!--      if (this.form.id){//更新-->
<!--        request.put("/api/shareparking",this.form).then(res =>{-->
<!--          console.log(res);-->
<!--          if (res.code==='0'){-->
<!--            this.$message({-->
<!--              type: "success",-->
<!--              message: "更新成功"-->
<!--            })-->
<!--          }else {-->
<!--            this.$message({-->
<!--              type: "error",-->
<!--              message: res.msg-->
<!--            })-->
<!--          }-->
<!--          this.dialogVisible = false;//关闭弹窗-->
<!--        });-->
<!--      }else {//新增-->
<!--        request.post("/api/shareparking",this.form).then(res =>{-->
<!--          console.log(res);-->
<!--          if (res.code==='0'){-->
<!--            this.$message({-->
<!--              type: "success",-->
<!--              message: "新增成功"-->
<!--            })-->
<!--          }else {-->
<!--            this.$message({-->
<!--              type: "error",-->
<!--              message: res.msg-->
<!--            })-->
<!--          }-->

<!--          this.dialogVisible = false;//关闭弹窗-->
<!--        });-->
<!--      }-->
<!--    },-->
<!--    handleEdit(row) {-->
<!--      this.form=JSON.parse(JSON.stringify(row));-->
<!--      this.dialogVisible=true;-->
<!--    },-->
<!--    handleDelete(id){-->
<!--      console.log(id);-->
<!--      request.delete("/api/shareparking/"+id).then(res => {-->
<!--        if (res.code==='0'){-->
<!--          this.$message({-->
<!--            type: "success",-->
<!--            message: "删除成功"-->
<!--          })-->
<!--        }else {-->
<!--          this.$message({-->
<!--            type: "error",-->
<!--            message: res.msg-->
<!--          })-->
<!--        }-->
<!--      });-->
<!--    },-->
<!--  }-->
<!--}-->
<!--</script>-->







<template>
  <nav style="padding: 10px">

    <div style="margin: 10px 0">
      <div v-if="tableData.length > 0">
        <el-button @click="add">
          点击发布车位
        </el-button>
      </div>
    </div>

    <el-dialog v-model="dialogVisible" title="Tips" width="30%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="小区">
          <el-input v-model="form.小区" style="width:80%"/>
        </el-form-item>
        <el-form-item label="车位号">
          <el-input v-model="form.sid" style="width:80%"/>
        </el-form-item>
        <el-form-item label="可用时间段">
          <el-input v-model="form.可用时间段" style="width:80%"/>
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
                确认发布
              </el-button>
            </span>
            </template>
          </el-dialog>
  </nav>
</template>

<script>
// @ is an alias to /src
//import HelloWorld from '@/components/HelloWorld.vue'

import request from "@/utils/request";

export default {
  name: 'Shareparking',
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
