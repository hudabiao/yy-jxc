<template>
  <view class="wrapper">
    <!-- 个人资料 -->
    <view>
      <view class="top">
        <view class="center">
          <view class="center_top">
            <view class="center_info">
              <view class="center_name">
                <view>{{userInfo.realName}}</view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
    <!-- 统计 -->
    <view class="count">
      <view class="cell"> {{ summary.sellAmount }} <text style="color: #AAAAAA;">数量</text> </view>
      <view class="cell"> {{ summary.totalIncome }} <text style="color: #AAAAAA;">总进账</text> </view>
      <view class="cell"> {{ summary.wechatIncome }} <text style="color: #AAAAAA;">微信</text> </view>
      <view class="cell"> {{ summary.alipayIncome }} <text style="color: #AAAAAA;">支付宝</text> </view>
      <view class="cell"> {{ summary.cashIncome }} <text style="color: #AAAAAA;">现金</text> </view>
      <view class="cell"> {{ summary.otherIncome }} <text style="color: #AAAAAA;">其他</text> </view>
    </view>
    <view class="extra">
      <view v-for="menu in menuList" @click="goto(menu.url)" class="item icon-arrow">{{ menu.name }}</view>
    </view>

    <view class="extra">
      <view @click="logout" class="item icon-arrow"> 注销 </view>
    </view>
  </view>
</template>

<script>
import { mapState } from 'vuex'
export default {
  data() {
    return {
      image:'',
      summary: {
        sellAmount: 0,
        totalIncome: 0,
        wechatIncome: 0,
        alipayIncome: 0,
        cashIncome: 0,
        otherIncome: 0
      },
    }
  },
  onShow(){
    this.summaryToday()
  },
  computed: {
    ...mapState({
      userInfo: state => state.userInfo,
      menuList: state => state.menuList
    })
  },
  methods: {
    async summaryToday(){
      uni.request({
        url: '/summary/today',
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.summary = resp.data;
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    async getUserInfo(){
      uni.request({
        url: '/sys/user/info',
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.userName = resp.data.realName;
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    async getMenuList(){
      uni.request({
        url: '/sys/menu/select',
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.menuList = resp.data;
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    goto(url) {
      // 可以跳多级目录
      uni.navigateTo({
        url
      })
    },
    logout(url) {
      uni.removeStorageSync('token');
      uni.reLaunch({
        url: "../login/login",
      })
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
  height: 80px;
  background: #23EBB9;
  padding-top: 15px;
  position: relative;
}

.center {
  width: 95%;
  height: 65px;
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
  height: 50px;
  margin: 0 auto;
  margin-top: 20rpx;
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
    font-size: 35rpx;
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
  margin: 20rpx 20rpx;
  background-color: #fff;
  border-radius: 4rpx;

  .item {
    line-height: 1;
    padding: 25rpx 0 25rpx 20rpx;
    border-bottom: 1rpx solid #eee;
    font-size: 30rpx;
    color: #333;
  }

  .item:active {
    background-color: #bcbfc2;
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

