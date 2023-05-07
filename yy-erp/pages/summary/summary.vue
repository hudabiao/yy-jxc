<template>
  <view>
    <uni-datetime-picker v-model="dateRange" type="daterange" @change="loadData"/>
    <view class="container" v-for="summary in summaryList">
      <view class="title">{{ summary.deptName }}</view>
      <uni-row>
        <uni-col :span="12">
          <view class="item">
            <view class="label">微信</view>
            <view class="value">{{ summary.wechatIncome }}</view>
          </view>
        </uni-col>
        <uni-col :span="12">
          <view class="item">
            <view class="label">支付宝</view>
            <view class="value">{{ summary.alipayIncome }}</view>
          </view>
        </uni-col>
        <uni-col :span="12">
          <view class="item">
            <view class="label">现金</view>
            <view class="value">{{ summary.cashIncome }}</view>
          </view>
        </uni-col>
        <uni-col :span="12">
          <view class="item">
            <view class="label">其他</view>
            <view class="value">{{ summary.otherIncome }}</view>
          </view>
        </uni-col>
        <uni-col :span="12">
          <view class="item">
            <view class="number">数量</view>
            <view class="value">{{ summary.sellAmount }}</view>
          </view>
        </uni-col>
        <uni-col :span="12">
          <view class="item">
            <view class="total">总计</view>
            <view class="value">{{ summary.totalIncome }}</view>
          </view>
        </uni-col>
        <uni-col :span="12">
          <view class="item">
            <view class="total">利润</view>
            <view class="value">{{ summary.totalProfit }}</view>
          </view>
        </uni-col>
      </uni-row>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      summaryList: [],
      dateRange: [Date.now(), Date.now()]
    }
  },
  onShow(){
    this.loadData()
  },
  methods: {
    loadData(dateRange) {
      if (dateRange) {
        this.dateRange = dateRange;
      } else {
        dateRange = this.dateRange;
      }
      uni.request({
        url: '/summary/all',
        method: 'get',
        data: {
          startDate: new Date(dateRange[0]).format("yyyy-MM-dd"),
          endDate: new Date(dateRange[1]).format("yyyy-MM-dd")
        },
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.summaryList = resp.data;
        },
        fail: function (err) {
          console.error(err);
        }
      });
    }
  }
}
</script>

<style>
.container{
  margin-bottom: 50rpx;
}
.title {
  padding-top: 20rpx;
  padding-left: 20rpx;
  font-size: 30rpx;
  /*font-weight: bold;*/
}

.item {
  padding-top: 20rpx;
  padding-left: 20rpx;
}

.label {
  display: inline-block;
  width: 150rpx;
  color: #999;
}

.number {
  display: inline-block;
  width: 150rpx;
  color: darkslategray;
}

.total {
  display: inline-block;
  width: 150rpx;
  color: red;
}

.value {
  display: inline-block;
  /*width: 20rpx;*/
}
</style>
