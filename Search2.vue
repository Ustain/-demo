<template>
  <div style="padding: 20px">
    <div style="margin: 0px 0">
      <el-input prefix-icon="search" v-model="search" placeholder="请输入小区名称" style="width: 20%" size="mini" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>

    <div class="radio-row" size="large">
      <div style="margin-right: 35px;">评分</div>
      <el-radio-group v-model="rateRange">
        <el-radio :label="Default" @click="resetRateRange">全部</el-radio>
        <el-radio label="0-3" @click="load">0-3</el-radio>
        <el-radio label="3-4" @click="load">3-4</el-radio>
        <el-radio label="4-5" @click="load">4-5</el-radio>
      </el-radio-group>
    </div>
    <div class="radio-row" size="large" style="margin-top: 20px">
      <div style="margin-right: 35px;">价格</div>
      <el-radio-group v-model="priceRange">
        <el-radio :label="Default" @click="resetRateRange">全部</el-radio>
        <el-radio label="0-5" @click="load">0-5</el-radio>
        <el-radio label="5-10" @click="load">5-10</el-radio>
        <el-radio label=">15" @click="load">>15</el-radio>
      </el-radio-group>
    </div>
    <div class="radio-row" size="large" style="margin-top: 20px">
      <div style="margin-right: 35px;">时间段</div>
      <el-radio-group v-model="timeRange">
        <el-radio :label="Default" @click="resetRateRange">全部</el-radio>
        <el-radio label="0-8" @click="load">0-8</el-radio>
        <el-radio label="8-16" @click="load">8-16</el-radio>
        <el-radio label="16-24" @click="load">>16-24</el-radio>
      </el-radio-group>
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
          <el-table-column prop="sid" label="车位号" width="180"/>
          <el-table-column prop="price" label="价格" sortable/>
          <el-table-column prop="tel" label="电话号码"/>
          <el-table-column prop="state" label="状态"/>

          <el-table-column prop="rate" label="评分" sortable/>
          <el-table-column prop="astartTime" label="可用开始时间"/>
          <el-table-column prop="aendTime" label="可用结束时间"/>

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
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Search2",
  data() {
    return {
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 10,
      price: 0,      // 价格选项的绑定数据
      score: 0,      // 评分选项的绑定数据
      astartTime: 0,  // 开始时间选项的绑定数据
      aendTime: 0,    // 结束时间选项的绑定数据
      tableData: [],
      priceRange: '', // 价格范围
      rateRange: '', // 评分范围
      timeRange: '', // 时间范围
    }
  },
  created() {
    this.load()
  },
  methods: {
    resetRateRange() {
      this.rateRange = ''; // Reset the rateRange value
      this.load(); // Reload the data
    },
    load() {
      const params = {
        pageNum: this.currentPage,
        pageSize: this.pageSize,
        search: this.search,
      };

      // 添加价格范围条件
      if (this.priceRange === '0-5') {
        params.minPrice = 0;
        params.maxPrice = 5;
      } else if (this.priceRange === '5-10') {
        params.minPrice = 5;
        params.maxPrice = 10;
      } else if (this.priceRange === '>15') {
        params.minPrice = 15;
      }

      // 添加评分范围条件
      if (this.rateRange === '0-3') {
        params.minRate = '0';
        params.maxRate = '3';
      } else if (this.rateRange === '3-4') {
        params.minRate = '3';
        params.maxRate = '4';
      } else if (this.rateRange === '4-5') {
        params.minRate = '4';
        params.maxRate = '5';
      }

      // 添加时间范围条件
      if (this.timeRange === '0-8') {
        params.minAstartTime = '0';
        params.maxAstartTime = '8';
      } else if (this.timeRange === '8-16') {
        params.minAstartTime = '8';
        params.maxAstartTime = '16';
      } else if (this.timeRange === '16-24') {
        params.minAstartTime = '16';
        params.maxAstartTime = '24';
      }

      // 发起请求
      request.get("/api/info", { params })
          .then(res => {
            console.log(res);
            this.tableData = res.data.records;

            // 添加评分范围过滤
            if (this.rateRange !== 'Default') {
              this.tableData = this.tableData.filter(item => {
                const rate = item.rate;
                if (this.rateRange === '0-3') {
                  return rate >= 0 && rate <= 3;
                } else if (this.rateRange === '3-4') {
                  return rate >= 3 && rate <= 4;
                } else if (this.rateRange === '4-5') {
                  return rate >= 4 && rate <= 5;
                }
                return true;
              });
            }

            // 添加时间范围过滤
            if (this.timeRange !== 'Default') {
              this.tableData = this.tableData.filter(item => {
                const astartTime = item.astartTime;
                if (this.timeRange === '0-8') {
                  return astartTime >= 0 && astartTime <= 8;
                } else if (this.timeRange === '8-16') {
                  return astartTime >= 8 && astartTime <= 16;
                } else if (this.timeRange === '16-24') {
                  return astartTime >= 16 && astartTime <= 24;
                }
                return true;
              });
            }
            console.log("Filtered tableData:", this.tableData); // 添加调试信息
            this.total = res.data.total;
          });
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
  }
}
</script>

<style scoped>
.radio-row {
  display: flex;
}
</style>
