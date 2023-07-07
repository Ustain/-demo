
<template>
  <nav style="padding: 10px">
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">发布车位</el-button>
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
          <el-table-column prop="neighbourhood" label="小区"/>
          <el-table-column prop="sid" label="车位号"/>
          <el-table-column prop="price" label="价格" sortable/>
          <el-table-column prop="tel" label="电话号码">
<!--            <template #default="scope">-->
<!--              <span v-if="scope.row.tel === tel">{{ scope.row.tel }}</span>-->
<!--            </template>-->
          </el-table-column>
          <el-table-column prop="astartTime" label="可用开始时间" width="180"/>
          <el-table-column prop="aendTime" label="可用结束时间" width="180"/>
          <el-table-column  label="图片">
            <template #default="scope">
              <el-image
                  style="width: 100px; height: 100px"
                  :src="scope.row.img"
                  :zoom-rate="1.2"
                  :preview-src-list="[scope.row.img]"
                  preview-teleported="true"
                  :initial-index="4"
                  fit="cover"/>
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
            <el-input v-model="form.neighbourhood" style="width:80%"/>
          </el-form-item>
          <el-form-item label="车位号">
            <el-input v-model="form.sid" style="width:80%"/>
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model="form.price" style="width:80%"/>
          </el-form-item>
          <el-form-item label="电话号码">
            <el-input v-model="tel" disabled style="width:80%"/>
          </el-form-item>
          <el-form-item label="可用开始时间">
            <el-input v-model="form.astartTime" style="width:80%"/>
          </el-form-item>
          <el-form-item label="可用结束时间">
            <el-input v-model="form.aendTime" style="width:80%"/>
          </el-form-item>
          <el-form-item label="图片">
            <el-upload
                ref="upload"
                action="http://localhost:9092/files/upload "
                :on-success="filesUploadSuccess">

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
    </div>
  </nav>
</template>

<script>


import request from "@/utils/request";

export default {
  name: 'Share',
  components: {},
  data() {
    return {
      form: {},
      tel:'',
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize:10,
      total: 10,
      // pageNum:0,
      tableData: [],
      infoData: [] // 添加这一行来初始化infoData为空数组
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}";
    this.form = JSON.parse(str);
    this.tel = this.form.tel;
    this.load()
  },
  methods: {
    // load(){
    //   request.get("/api/share", {
    //     params: {
    //       pageNum: this.currentPage,
    //       pageSize: this.pageSize,
    //       search: this.search
    //     }
    //   }).then(res =>{
    //     console.log(res);
    //     this.tableData = res.data.records.filter(item => item.tel === this.tel); // 筛选出tel和当前user的tel相同的行;
    //     this.total = this.tableData.length; // 更新total为筛选后的行数
    //
    //   })
    // },

    load() {
      request.get("/api/share", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res);
        const shareData = res.data.records.filter(item => item.tel === this.tel); // 筛选出tel和当前user的tel相同的行;

        // 获取相同neighbourhood和sid的info表中state为"已预定"的数据的集合
        const reservedData = this.infoData.filter(infoItem => {
          return infoItem.state === "已预定" && shareData.some(shareItem => shareItem.neighbourhood === infoItem.neighbourhood && shareItem.sid === infoItem.sid);
        });

        this.tableData = shareData.filter(shareItem => {
          return !reservedData.some(reservedItem => reservedItem.neighbourhood === shareItem.neighbourhood && reservedItem.sid === shareItem.sid);
        });

        this.total = this.tableData.length; // 更新total为筛选后的行数
      });
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
        this.form.tel = this.tel;
        request.put("/api/share",this.form).then(res =>{
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
        this.form.tel = this.tel;
        request.post("/api/share",this.form).then(res =>{
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
      request.delete("/api/share/"+id).then(res => {
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

