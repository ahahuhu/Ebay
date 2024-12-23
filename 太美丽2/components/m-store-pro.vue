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
		<view class="m-distance">
			<image @click="updateOnSale(rowData.id)" style="width:40upx;height: 40upx;"
				src="@/static/icon/shop_icon_reduce.png" mode="aspectFit"></image>
		</view>
		<view class="m-distance">
			<image @click="touchOnGoods(rowData.id)" style="width:40upx;height: 40upx;"
				src="@/static/icon/shop_icon_buy.png" mode="aspectFit"></image>
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
			onItemClick() {
				// 从props中获取rowData.id并赋值给productId
				const productIddd = this.rowData.id;
				console.log(productIddd); // 输出productId以确保它被正确赋值
				// 跳转到index页面，并传递productId作为参数
				uni.navigateTo({
					url: `/pages/home/commodity?id=${productIddd}`
				});
			},
			updateOnSale(id) {
				const value=uni.getStorageSync("userinfo")
				const userID=value.id
				console.log("用户ID："+userID)
				console.log('Updating on sale status for product:', id);
				//const userID = 10; // 用户ID，这里使用的是固定值，实际应用中应该从父组件或Vuex获取
				uni.request({
					url: 'http://localhost:8016/changeProduct',
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
							// 更新成功后的处理
							console.log('Product on sale status updated');
							uni.showToast({
								title: '商品已经售出',
								icon: 'success',
								duration: 2000
							});
							// 设置延时，延时结束后执行删除收藏和刷新页面的逻辑
							setTimeout(() => {
								this.touchOnGoods(id);
							}, 2000); // 延时2000毫秒（即2秒）
						} else {
							// 处理错误情况
							console.error('Failed to update on sale status:', res);
							uni.showToast({
								title: '商品售出更新失败',
								icon: 'none',
								duration: 2000
							});
						}
					},
					fail: (err) => {
						// 网络请求失败的处理
						console.error('Request failed:', err);
						uni.showToast({
							title: '网络请求失败',
							icon: 'none',
							duration: 2000
						});
					}
				});
			},

			touchOnGoods(id) {
				const value=uni.getStorageSync("userinfo")
				const userID=value.id
				console.log("用户ID："+userID)
				console.log(this.rowData);
				//const userID = 10; // 用户ID，这里使用的是固定值，实际应用中应该从父组件或Vuex获取
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
						if (res.data.code == 200) {
							// 删除成功后的处理，例如提示用户
							console.log('Product removed from favorites');
							// 重新启动应用并跳转到当前页面
							uni.reLaunch({
								url: '/pages/my/store' // 替换为当前页面的路径
							});
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