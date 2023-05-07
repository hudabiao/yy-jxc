<template>
  <view class="wrapper">

    <!-- 个人资料 -->
    <view>
      <view class="top">
        <view class="center">
          <view class="center_top">
            <view class="center_img" @tap="gotoFeeds('/pages/my/set/set')">
              <!-- 这里可以放自己的静态头像 -->
              <!-- #ifndef MP-WEIXIN -->
              <image :src="image"></image>
              <!-- #endif -->
              <open-data type="userAvatarUrl" class="user_head"></open-data>
            </view>
            <view class="center_info" @tap="gotoFeeds('/pages/my/set/set')">
              <view class="center_name">
                <!-- 这里可以放自己的名称图片 -->
                <!-- #ifndef MP-WEIXIN -->
                <view>{{userInfo.name}}</view>
                <!-- #endif -->
                <open-data class="nickname" type="userNickName"></open-data>
              </view>
              <view class="center_vip">
                <image class="rank_icon" src="@/static/icon/vip.png" />
                <view class="vip_text">
                  <text>普通会员</text>
                </view>
              </view>
            </view>
            <view style="margin-left: 140rpx;margin-top: 15rpx;"  @tap="gotoFeeds('/pages/my/set/set')">
              <image style="width: 30px;height: 30px;" src="@/static/icon/setting.png"></image>
            </view>
          </view>
        </view>
        <image src='@/static/icon/waterflow.gif' mode='scaleToFill' class='gif-wave'></image>
      </view>
    </view>


    <!-- 统计 -->
    <view class="count">
      <view class="cell"> 8 <text style="color: #AAAAAA;">收藏店铺</text> </view>
      <view class="cell"> 14 <text style="color: #AAAAAA;">收藏商品</text> </view>
      <view class="cell"> 18 <text style="color: #AAAAAA;">关注商品</text> </view>
      <view class="cell"> 84 <text style="color: #AAAAAA;">我的足迹</text> </view>
    </view>
    <!-- 我的订单 -->
    <view class="orders">
      <view class="title">我的订单</view>
      <view class="sorts">

        <view style="margin-top: -10rpx;margin-left: 60rpx;" @tap="gotoFeeds('/pages/sort/sort')">
          <image src='@/static/icon/nopay.png' style="width: 30px;height: 30px;"></image>
        </view>
        <view style="margin-top: 60rpx;margin-left: -60rpx;margin-bottom: -20rpx;" @tap="gotoFeeds('/pages/sort/sort')">
          <text class="icon-bill">待付款</text>
        </view>

        <view style="margin-top: -15rpx;margin-left: 95rpx;" @tap="gotoFeeds('/pages/sort/sort')">
          <image src='@/static/icon/noreceive.png' style="width: 40px;height: 40px;"></image>
        </view>
        <view style="margin-top: 60rpx;margin-left: -75rpx;margin-bottom: -20rpx;" @tap="gotoFeeds('/pages/sort/sort')">
          <text class="icon-car">待收货</text>
        </view>

        <view style="margin-top: -15rpx;margin-left: 95rpx;" @tap="gotoFeeds('/pages/sort/sort')">
          <image src='@/static/icon/noreturn.png' style="width: 40px;height: 40px;"></image>
        </view>
        <view style="margin-top: 60rpx;margin-left: -80rpx;margin-bottom: -20rpx;" @tap="gotoFeeds('/pages/sort/sort')">
          <text class="icon-money">待归还</text>
        </view>

        <view style="margin-top: -15rpx;margin-left: 95rpx;" @tap="gotoFeeds('/pages/sort/sort')">
          <image src='@/static/icon/allorder.png' style="width: 40px;height: 40px;"></image>
        </view>
        <view style="margin-top: 60rpx;margin-left: -85rpx;margin-bottom: -20rpx;" @tap="gotoFeeds('/pages/sort/sort')">
          <text class="icon-list">全部订单</text>
        </view>

      </view>
    </view>
    <!-- 地址管理 -->
    <view class="address icon-arrow">
      收货地址
    </view>
    <!-- 其它 -->
    <view class="extra">
      <view @click="makePhone" class="item icon-arrow">联系客服</view>
      <button open-type="feedback" class="item icon-arrow">意见反馈</button>
    </view>
  </view>
</template>

<script>
// 引入vuex
import {
  mapState
} from 'vuex';

export default {
  data() {
    return {
      image:'',
    }
  },
  // 得到属性userInfo
  computed: {
    ...mapState(['userInfo'])
  },
  created(){
    this.selectImageUrl()
  },
  methods: {
    async selectImageUrl(){
      let res = await this.$u.api.selectImageUrl({
        params: {
          user: this.userInfo.name
        }
      })
      this.image = res.data.data
      console.log("this.image ",this.image )
    },
    gotoFeeds(url) {
      // 可以跳多级目录
      uni.navigateTo({
        url
      })
      //跳转同级路由目录
      // uni.switchTab({
      // 	url
      // })
    }
  }
};
</script>

<style scoped lang="scss">
Page {
  font-size: 14px;
}

.top {
  width: 100%;
  height: 130px;
  background: #23EBB9;
  padding-top: 15px;
  position: relative;
}

.center {
  width: 95%;
  height: 100px;
  background: white;
  display: flex;
  flex-direction: column;
  margin: 0 auto;
  border-radius: 5px;
}

.center_top {
  display: flex;
  flex-direction: row;
  width: 80%;
  height: 80px;
  margin: 0 auto;
  margin-top: 20rpx;
  border-bottom: 1px solid #EEEEEE;
}

.center_img {
  width: 66px;
  height: 66px;
  border-radius: 50%;
  overflow: hidden;
}

.center_img image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.center_img .user_head {
  width: 100%;
  height: 100%;
}

.center_info {
  display: flex;
  flex-direction: column;
  margin-top: 20rpx;
  margin-left: 30px;
}

.center_name {
  font-size: 20px;
}

.center_phone {
  color: #BEBEBE;
}

// .center_down {
// 	display: flex;
// 	flex-direction: row;
// 	width: 80%;
// 	height: 35px;
// 	margin: 0 auto;
// 	margin-top: 20rpx;
// }

.center_rank {
  width: 50%;
  height: 35px;
  display: flex;
  flex-direction: row;
}

.rank_text {
  height: 35px;
  line-height: 35px;
  margin-left: 10rpx;
  color: #AAAAAA;
}

.center_vip image {
  width: 25px;
  height: 25px;
  margin-top: 15rpx;
}

.vip_icon {
  width: 25px;
  height: 25px;
  margin-top: -10rpx;
}

.vip_text {
  margin-top: -55rpx;
  margin-left: 50rpx;
  color: #AAAAAA;
}

.center_rank image {
  width: 35px;
  height: 35px;
}

.center_score {
  width: 50%;
  height: 35px;
  display: flex;
  flex-direction: row;
}

.center_score image {
  width: 35px;
  height: 35px;
}

.gif-wave {
  position: absolute;
  width: 100%;
  bottom: 0;
  left: 0;
  z-index: 99;
  mix-blend-mode: screen;
  height: 100rpx;
}

.wrapper {
  position: absolute;
  top: 0;
  bottom: 0;

  width: 100%;
  background-color: #F4F4F4;
}

.profile {
  height: 375rpx;
  background-color: #ea4451;
  display: flex;
  justify-content: center;
  align-items: center;

  .meta {
    .avatar {
      display: block;
      width: 140rpx;
      height: 140rpx;
      border-radius: 50%;
      border: 2rpx solid #fff;
      overflow: hidden;
    }

    .nickname {
      display: block;
      text-align: center;
      margin-top: 20rpx;
      font-size: 30rpx;
      color: #fff;
    }
  }
}

.count {
  display: flex;
  margin: 0 20rpx;
  height: 120rpx;
  text-align: center;
  border-radius: 4rpx;
  background-color: #fff;

  position: relative;
  top: 10rpx;

  .cell {
    margin-top: 10rpx;
    flex: 1;
    padding-top: 20rpx;
    font-size: 27rpx;
    color: #333;
  }

  text {
    display: block;
    font-size: 24rpx;
  }
}

.orders {
  margin: 20rpx 20rpx 0 20rpx;
  padding: 40rpx 0;
  background-color: #fff;
  border-radius: 4rpx;

  .title {
    padding-left: 20rpx;
    font-size: 30rpx;
    color: #333;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #eee;
    margin-top: -30rpx;
  }

  .sorts {
    padding-top: 30rpx;
    text-align: center;
    display: flex;
  }

  [class*="icon-"] {
    flex: 1;
    font-size: 24rpx;

    &::before {
      display: block;
      font-size: 48rpx;
      margin-bottom: 8rpx;
      color: #ea4451;
    }
  }
}

.address {
  line-height: 1;
  background-color: #fff;
  font-size: 30rpx;
  padding: 25rpx 0 25rpx 20rpx;
  margin: 10rpx 20rpx;
  color: #333;
  border-radius: 4rpx;
}

.extra {
  margin: 0 20rpx;
  background-color: #fff;
  border-radius: 4rpx;

  .item {
    line-height: 1;
    padding: 25rpx 0 25rpx 20rpx;
    border-bottom: 1rpx solid #eee;
    font-size: 30rpx;
    color: #333;
  }

  button {
    text-align: left;
    background-color: #fff;

    &::after {
      border: none;
      border-radius: 0;
    }
  }
}

.icon-arrow {
  position: relative;

  &::before {
    position: absolute;
    top: 50%;
    right: 20rpx;
    transform: translateY(-50%);
  }
}
</style>

