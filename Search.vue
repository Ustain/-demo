<template>
  <div>
    <nav style="padding: 10px">
      <div style="margin: 10px 0">
      <el-radio-group v-model="searchRank" size="large">
        <el-radio-button label="Default" @click="handleSortClick('Default')">默认</el-radio-button>
        <el-radio-button label="Popularity">空闲中</el-radio-button>
        <el-radio-button label="SearchVolume" @click="handleSortClick('SearchVolume')">浏览量</el-radio-button>
        <el-radio-button  label="Rentals" @click="handleSortClick('Rentals')">预定量</el-radio-button>
      </el-radio-group>
    </div>

      <div style="margin: 10px 0">
        <el-input prefix-icon="search" v-model="search" placeholder="请输入内容" style="width: 20%" size="mini" clearable></el-input>
        <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      </div>

      <div style="margin: 10px 0">
        <div v-if="tableData.length > 0">
          <el-table
              :data="filteredTableData"
              border
              style="width: 100%">
            <el-table-column prop="id" label="ID" width="180" sortable/>
            //删掉width就自适应
            <el-table-column prop="neighbourhood" label="小区"/>
            <el-table-column prop="sid" label="车位号" width="180"/>
            <el-table-column prop="price" label="价格" sortable/>
            <el-table-column prop="tel" label="电话号码"/>
            <el-table-column prop="state" label="状态">
              <template #default="{ row }">
                <span v-if="row.state === '空闲中'">空闲中</span>
                <span v-else>{{ row.state }}</span>
              </template>
            </el-table-column>

            <el-table-column prop="rate" label="评分" sortable/>
            <el-table-column prop="astartTime" label="可用开始时间"/>
            <el-table-column prop="astartTime" label="可用结束时间"/>

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
                <el-button size="small" @click="handleDetails(scope.row)">详情</el-button>

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
      </div>
    </nav>

    <!-- 显示搜索结果 -->
    <ul>
      <li v-for="item in searchResult" :key="item.id">{{ item.name }}</li>
    </ul>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {
      searchRank: '',
      searchResult: [],
      search: '',
      tableData: [],
      filteredTableData: [], // 添加 filteredTableData 属性
      form: {},
      currentPage: 1,
      pageSize:10,
      total: 10,
      sortKey: '', // 添加 sortKey 属性
      sortDirection: 'desc', // 默认按照降序排序
      // nums:'',
      defaultSortKey: 'Default',
      // neighbourhood: '',
      // sid:1,
    };
  },

  watch: {
    searchRank() {
      this.filterTableData(); // 监听 searchRank 的变化，重新筛选车位数据
    },
  },
  created() {
    console.log("created() 方法被调用");
    this.getNumsData2(); // 调用获取 nums 数据的方法
    this.load()
  },
  mounted() {
    this.searchRank = this.defaultSortKey; // 设置默认选中项为 "默认"
  },
  methods: {
    getNumsData2() {
      request
          .get("/api/nums3", {

          })
          .then((response) => {
            const data = response.data;
            console.log("从后端接口获取到的 nums 数据:", data); // 打印数据到控制台，检查数据是否正确
            this.nums = data.nums; // 将接口返回的 nums 数据赋值给 nums 变量
          })
          .catch((error) => {
            console.error(error);
          });
    },

    handleSortClick(sortKey) {
      console.log("点击的排序方式:", sortKey); // 打印点击的排序方式
      if (sortKey === this.sortKey) {
        // 如果点击的是当前的排序方式，则切换排序方向
        this.sortDirection = this.sortDirection === 'desc' ? 'asc' : 'desc';
      } else {
        // 如果点击的是新的排序方式，则设置新的排序方式和默认排序方向
        this.sortKey = sortKey;
        this.sortDirection = 'desc';
      }
      // 判断是否点击了默认按钮
      if (sortKey === 'Default') {
        this.sortKey = ''; // 重置sortKey为初始值
        this.sortDirection = 'desc'; // 重置sortDirection为初始值
        this.load(); // 重新加载表格数据
      }else {
        this.filterTableData();
      }
    },
    sortTableDataByNums() {
      // 按照 nums 字段排序
      this.tableData.sort((a, b) => {
        const aNums = parseInt(a.nums) || 0; // 将 a.nums 转换为数字类型，如果转换失败则设为 0
        const bNums = parseInt(b.nums) || 0; // 将 b.nums 转换为数字类型，如果转换失败则设为 0
        return aNums - bNums;
      });
    },

    filterTableData() {
      console.log("排序方式:", this.sortKey); // 打印当前的排序方式
      console.log("排序方向:", this.sortDirection); // 打印当前的排序方向

      if (this.searchRank === 'Popularity') {
        // 筛选状态为 "空闲中" 的车位数据
        this.filteredTableData = this.tableData.filter(item => item.state === '空闲中');
      } else {
        // 不进行筛选，显示所有车位数据
        this.filteredTableData = this.tableData;
      }

      if (this.sortKey === 'SearchVolume') {
        // 根据浏览量（count2）进行排序
        this.filteredTableData.sort((a, b) => {
          if (this.sortDirection === 'desc') {
            return a.count2 - b.count2;
          } else {
            return b.count2 - a.count2;
          }
        });
      }

      if (this.sortKey === 'Rentals') {
        // 根据 nums 进行排序
        this.filteredTableData.sort((a, b) => {
          const aNums = parseInt(a.nums) || 0; // 将 a.nums 转换为数字类型，如果转换失败则设为 0
          const bNums = parseInt(b.nums) || 0; // 将 b.nums 转换为数字类型，如果转换失败则设为 0
          if (this.sortDirection === 'asc') {
            return aNums - bNums;
          } else {
            return bNums - aNums;
          }
        });
      }
      console.log("筛选后的表格数据:", this.filteredTableData); // 打印筛选后的表格数据
    },


    load(){

      request.get("/api/info", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
        }
      }).then(res =>{
        console.log(res);
        this.tableData = res.data.records;
        this.tableData.forEach(item => {
          const key = `${item.neighbourhood}_${item.sid}`;
          const count2Obj = JSON.parse(localStorage.getItem('count2Obj')) || {};
          item.count2 = count2Obj[key] || 0;
          item.nums = item.nums || 0; // 如果没有 nums 字段，则将其设为 0
        });
        this.sortTableDataByNums(); // 按照 nums 字段排序和分组
        this.filterTableData(); // 筛选车位数据
        this.total = res.data.total;
      })
    },
    handleDetails(row) {
      // 从 localStorage 中读取 count2 对象
      let count2Obj = JSON.parse(localStorage.getItem('count2Obj')) || {};

      // 构建组合键
      const key = `${row.neighbourhood}_${row.sid}`;

      // 获取组合键对应的 count2 值
      let count2 = count2Obj[key] || 0;

      // 自增 count2 的值
      count2++;

      // 输出 count2 的值进行调试
      console.log(`Current count2 for ${key}:`, count2);

      // 更新 count2Obj 对象
      count2Obj[key] = count2;

      // 将更新后的 count2Obj 对象存储回 localStorage
      localStorage.setItem('count2Obj', JSON.stringify(count2Obj));

      // 根据小区名称找到相同小区的数据
      const sameNeighbourhood = this.tableData.filter(item => item.neighbourhood === row.neighbourhood);
      // 计算相同小区的价格均值
      const avgPrice = sameNeighbourhood.reduce((sum, item) => sum + item.price, 0) / sameNeighbourhood.length;
      const count = sameNeighbourhood.length;
      // 导航到目标路由
      this.$router.push({ path: '/details', query: { ...row, avgPrice: avgPrice, count: count, count2: count2 } });
    },


    handleDelete(id){
      console.log(id);
      request.delete("/api/info/"+id).then(res => {
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
    },


  },
};
</script>
