<template>
	<view>
    <uni-data-select :localdata="categoryList" @change="changeCategory"></uni-data-select>
		<uni-list>
		  <uni-list-chat v-for="(item,index) in inventoryList"
		                 :title="item.goodsName"
                     :clickable="true"
                     @click="showImage(index)"
		                 :note="item.category"
		                 :avatar="item.picture">
		    <view class="chat-custom-right">
		      <text class="chat-custom-text">零售价：{{ item.sellingPrice }}</text>
		      <text class="chat-custom-text">存量：{{ item.amount }}</text>
		    </view>
		  </uni-list-chat>
		</uni-list>
	</view>
</template>

<script>
export default {
  data() {
    return {
      inventoryList: [],
      categoryList: [],
      categoryId: '',
      imageList:[]
    }
  },
  mounted() {
    this.loadCategory();
  },
  onShow(){
    this.loadData();
  },
  methods: {
    showImage(index){
      uni.previewImage({
        current: index,
        urls: this.imageList
      })
    },
    changeCategory(categoryId){
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
          this.categoryList = resp.data.map(c=>{
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
        url: '/shop-inventory/list',
        method: 'get',
        data: {
          categoryId: this.categoryId
        },
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.inventoryList = resp.data;
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
