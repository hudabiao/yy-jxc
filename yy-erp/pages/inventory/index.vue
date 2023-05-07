<template>
  <view>
    <view class="box-bg">
      <uni-nav-bar @clickRight="exportCode">
        <view class="input-view">
          <uni-datetime-picker type="date" v-model="date" @change="changeDate"/>
        </view>
        <template v-slot:right>
          <view class="city">
            导出条码
          </view>
        </template>
      </uni-nav-bar>
    </view>
    <uni-list>
      <uni-list-chat v-for="(item,index) in inventoryList"
                     :title="item.goodsName"
                     :note="item.barCode"
                     :clickable="true"
                     @click="showImage(index)"
                     :avatar="item.picture">
        <view class="chat-custom-right">
          <text class="chat-custom-text">{{ item.createTime }}</text>
          <text class="chat-custom-text">售价：{{ item.sellingPrice }} &nbsp;&nbsp; 数量：{{ item.amount }}</text>
        </view>
      </uni-list-chat>
    </uni-list>
  </view>
</template>

<script>
// import View from "../component/view/view";
import store from '@/store/index.js'

export default {
  // components: {View},
  data() {
    return {
      inventoryList: [],
      date: '',
      category: '',
      showCheck: true,
      imageList: [],
      page: 1
    }
  },
  onShow() {
    this.loadData();
  },
  onPullDownRefresh(){
    this.page = this.page+1;
    this.loadData();
  },
  methods: {
    showImage(index){
      uni.previewImage({
        current: index,
        urls: this.imageList
      })
    },
    changeDate(date){
      this.date = date;
      this.inventoryList = [];
      this.imageList = [];
      this.loadData();
    },
    exportCode() {
      const token = store.state.token;
      uni.showLoading({
        title: '加载中'
      });
      uni.downloadFile({
        // url: 'http://localhost:8081/renren-api/goods/export/code?date=' + this.date,
        url: 'https://www.dodomido.cc/yy/goods/export/code?date=' + this.date,
        header: {
          token: token
        },
        success: (res) => {
          console.log('res', res);
          if (res.statusCode === 200) {
            uni.saveFile({
              tempFilePath: res.tempFilePath,
              success: function (res) {
                uni.hideLoading();
                var savedFilePath = res.savedFilePath;
                console.log('savedFilePath', savedFilePath);
                uni.openDocument({
                  filePath: savedFilePath,
                  showMenu: true,
                  success: function (res) {
                    console.log('打开文档成功');
                  },
                  fail: function (res) {
                    uni.showToast({title: "打开文件失败：" + filepathss, icon: 'error', duration: 2000});
                    console.log(res);
                  }
                });
              }
            });
          }
        }
      });
    },
    loadData() {
      uni.request({
        url: '/goods/list',
        method: 'get',
        data:{
          createDate: this.date,
          pageNumber: this.page
        },
        header: {
          'token': ''
        },
        success: (res) => {
          uni.stopPullDownRefresh();
          const resp = res.data;
          let data = resp.data;
          if(data && data.length > 0 ){
            data.reverse();
            this.inventoryList.unshift(...data);
            const imgList = data.map(goods=> goods.picture)
            this.imageList.unshift(imgList);
          }
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
.chat-custom-right {
  flex: 1;
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-end;
}

.chat-custom-text {
  font-size: 24rpx;
  color: #999;
}

</style>
