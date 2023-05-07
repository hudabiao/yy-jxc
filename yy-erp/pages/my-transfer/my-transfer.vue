<template>
  <view>
    <uni-data-select :localdata="categoryList" v-model="categoryId" @change="changeCategory"></uni-data-select>
    <uni-list>
      <uni-list-chat v-for="(item,index) in myTransferList"
                     :title="item.goodsName"
                     :clickable="true"
                     @click="showImage(index)"
                     :note="'数量：'+item.amount"
                     :avatar="item.picture">
        <view class="chat-custom-right">
          <button size="mini" type="primary" @click.stop="confirmImport(item)">确认入库</button>
          <text class="chat-custom-text">条码：{{ item.barCode }}</text>
        </view>
      </uni-list-chat>
    </uni-list>
  </view>
</template>

<script>
export default {
  data() {
    return {
      categoryId: '',
      categoryList: [],
      myTransferList: [],
      imageList:[]
    }
  },
  onShow() {
    this.loadData();
  },
  onLoad(){
    this.loadCategory();
  },
  methods: {
    showImage(index){
      uni.previewImage({
        current: index,
        urls: this.imageList
      })
    },
    changeCategory(categoryId) {
      this.categoryId = categoryId;
      this.loadData();
    },
    loadCategory() {
      uni.request({
        url: '/category/list',
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.categoryList = resp.data.map(c => {
            return {
              value: c.id,
              text: c.category
            }
          });
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    loadData() {
      uni.request({
        url: '/shop-inventory/my-transfer',
        method: 'get',
        data: {
          categoryId: this.categoryId
        },
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.myTransferList = resp.data;
          this.imageList = resp.data.map(goods=> goods.picture);
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    confirmImport(item){
      const _this = this;
      uni.showModal({
        title: '提示',
        content: '确认入库吗？',
        success: function (res) {
          if (res.confirm) {
            _this.transferImport(item);
          }
        }
      });
    },
    transferImport(item) {
      uni.request({
        url: `/shop-inventory/import-transfer/${item.id}`,
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          if (resp.code === 10000) {
            uni.showToast({title: '入库成功', icon: 'success', duration: 500});
            const index = this.myTransferList.indexOf(item)
            this.myTransferList.splice(index, 1);
          } else {
            uni.showToast({title: "入库失败", icon: 'error', duration: 2000});
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
