<template>
  <view>
    <view v-if="!sysUser">
      <view class="example-body">
        <uni-datetime-picker type="date" :clear-icon="false" v-model="recordDate" @change="loadData"/>
      </view>
      <uni-list>
        <uni-list-chat v-for="(item,index) in recordList"
                       :title="item.goodsName"
                       :clickable="true"
                       @click="showImage(index)"
                       :note="item.sellTime"
                       :avatar="item.picture">
          <view class="chat-custom-right">
            <text class="chat-custom-text">售价：{{ item.actualPaidPrice }} &nbsp;&nbsp;数量：{{ item.sellCount }}</text>
            <text class="chat-custom-text">收款方式：{{ item.paymentMethod }}</text>
            <button size="mini" type="primary" @click.stop="confirmRollback(item.id)">销售回退</button>
          </view>
        </uni-list-chat>
      </uni-list>
    </view>
    <view v-else>
      此页面是”店铺销售记录页“，管理账户不展示
    </view>
  </view>
</template>

<script>
import {mapState} from 'vuex'

export default {
  data() {
    return {
      recordDate: Date.now(),
      recordList: [],
      sysUser: false,
      imageList:[]
    }
  },
  computed: {
    ...mapState({
      menuList: state => state.menuList
    })
  },
  onLoad(){
    const index = this.menuList.findIndex(menu=>{
      return menu.url === '/pages/inventory/index'
    });
    if(index>=0){
      this.sysUser=true
    }
  },
  onShow() {
    this.loadData();
  },
  methods: {
    confirmRollback(id){
      const _this = this;
      uni.showModal({
        title: '提示',
        content: '确认回退此销售记录吗？',
        success: function (res) {
          if (res.confirm) {
            _this.rollbackSell(id);
          }
        }
      });
    },
    rollbackSell(id){
      uni.request({
        url: `/shop-inventory/sell/rollback/${id}`,
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          if (resp.code === 10000) {
            uni.showToast({title: '已完成回退', icon: 'success', duration: 500});
            this.loadData();
          } else {
            uni.showToast({title: "回退失败", icon: 'error', duration: 2000});
          }
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    showImage(index){
      uni.previewImage({
        current: index,
        urls: this.imageList
      })
    },
    loadData(date) {
      if (date) {
        this.recordDate = date;
      }
      const params = new Date(this.recordDate).format("yyyy-MM-dd");
      uni.request({
        url: '/shop-inventory/sell/record',
        method: 'get',
        data: {
          recordDate: params
        },
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.recordList = resp.data;
          this.imageList = resp.data.map(goods=> goods.picture);
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
