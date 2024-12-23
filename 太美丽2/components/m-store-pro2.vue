<template>
	<!-- 门店商品图文 -->
	<view class="m-store-item" @click="onItemClick(rowData.id)">
		<view class="m-img">
			<image style="width: 100%;height: 100%;" :src="rowData.img" mode="aspectFit"></image>
		</view>
		<view class="m-text">
			<view class="m-title">
				{{rowData.name}}
			</view>
			<view class="m-descripe">
				{{rowData.descripe}}
			</view>
			<view class="m-price">
				{{rowData.price}}
			</view>
		</view>
		
	</view>
</template>

<script>
export default {
  name: "m-store-pro",
  props: {
    rowData: {
      type: Object,
      default: () => ({})
    }
  },
  methods: {
    onItemClick(productId) {
      // 这里可以添加点击商品时的逻辑，例如跳转到商品详情页
      console.log('Item clicked with productId:', productId);
	  uni.navigateTo({
	  	url:`/pages/home/commodity?id=${productId}`
	  })
      // 示例：使用uni.navigateTo跳转到商品详情页
      // uni.navigateTo({
      //   url: `/pages/product-detail/product-detail?id=${productId}`
      // });
    },
    touchOnGoods(id) {
      console.log(this.rowData);
      // 假设从父组件接收到的rowData中包含了商品ID
      const userID = 10; // 用户ID，这里使用的是固定值，实际应用中应该从父组件或Vuex获取
      uni.request({
        url: 'http://localhost:8016/deleteFavorite',
        method: 'GET',
        data: {
          userID: userID,
          id: id
        },
        header: {
          'content-type': 'application/json'
        },
        success: (res) => {
          if (res.data.code === 200) {
            // 删除成功后的处理，例如提示用户
            console.log('Product removed from favorites');
          } else {
            // 处理错误情况
            console.error('Failed to remove product from favorites:', res);
          }
        },
        fail: (err) => {
          // 网络请求失败的处理
          console.error('Request failed:', err);
        }
      });
    }
  },
  data() {
    return {
      // 可以在这里定义组件内部需要的数据
    };
  }
}
</script>

<style lang="scss">
	.m-store-item {
		display: flex;
		flex-direction: row;
		width: 100%;
		justify-content: space-between;
		align-items: flex-end;
		margin-top: 30upx;
		margin-bottom: 30upx;
		//页面左边距*************
		margin-left: 30upx;

		//图片**************
		.m-img {
			flex: 0 0 170upx;
			height: 130upx;
			background: #eee;
			align-self: flex-start;
		}

		.m-text {
			flex: 1;
			padding: 0 20upx;

			.m-title {
				font-size: 32upx;
				color: #4c4c4c;
			}

			.m-descripe {
				font-size: 24upx;
				color: #999999;
				margin-top: 10upx;
			}

			.m-price {
				font-size: 28upx;
				color: #ff582b;
				font-weight: bold;
				margin-top: 5upx;
			}

			// .m-old-price{
			// 	display: flex;
			// 	flex-direction: row;
			// 	font-size: 18upx;
			// 	color:#999999;
			// 	margin-top: 5upx;
			// 	.m-num{
			// 		// font-size:  20upx;
			// 	}
			// }
		}

		.m-distance {
			flex: 0 1 30upx;
			color: #b2b2b2;
			font-size: 20upx;
			margin-right: 50upx;
			text-align: right
		}
	}
</style>