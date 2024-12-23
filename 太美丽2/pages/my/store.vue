<template>
	<view>
	<!-- 返回按钮 -->
	<button class="back-button" @click="goBack">
		<uni-icons type="left" size="25" style="position: relative;"></uni-icons>
		<u-icon name="arrow-left"></u-icon>
		返回
	</button>
	<view class="kongbai"></view>
	
	<view class="category-list">
		<view class="category" v-for="(category, index) in categoryList" :key="category.id">
			<view class="list">
				<view class="box" v-for="(box, i) in category.list" :key="i">
					<!-- 商品列表 -->
					<m-store-pro @touchOnGoods="touchOnGoods" :rowData="box"></m-store-pro>
				</view>
			</view>
		</view>
	</view>
	</view>


	<!-- <view class="good_box" v-show="!hide_good_box"  :style="'left:'+bus_x+'px;top:'+bus_y+'px'"></view> -->
	</view>
</template>
<script>
	import mFooterCar from '@/components/m-footer-car' // 底部
	import mStorePro from '@/components/m-store-pro' // 商品列表
	import uniNumberBox from "@/components/uni-number-box/uni-number-box.vue" // 加减数字
	export default {
		components: {
			mStorePro,
			uniNumberBox,
			mFooterCar
		},
		data() {
			return {
				specClass: '', //规格弹窗css类，控制开关动画
				showCategoryIndex: 0,
				categoryList: [],
				userID: 10,
			}
		},
		methods: {
			goBack() {
				console.log("返回上一级页面");
				uni.switchTab({
					url:'/pages/my/my'
				})
			},
			sendUserIdToBackend(userID) {
				uni.request({
					url: 'http://localhost:8016/allFavorite',
					method: 'GET',
					data: {
						userID: this.userID
					},
					header: {
						'content-type': 'application/json'
					},
					success: (res) => {
						if (res.data.code == 200) {
							// 假设后端返回的数据格式是 { productIds: ['id1', 'id2', 'id3'] }
							const ids = res.data.result;
							// 获取商品信息
							this.fetchProductInfo(ids);
							console.log(ids)
						} else {
							console.error('Failed to receive product IDs:', res);
						}
					},
					fail: (err) => {
						console.error('Request failed:', err);
					}
				});
			},
			fetchProductInfo(ids) {
				this.categoryList = [];
				const requests = ids.map(id => {
					return new Promise((resolve, reject) => {
						// 调用/file/productDownloadViaId接口以下载图片
						this.downloadAndConvertImage(id).then(base64Image => {
							// 图片下载并转码成功后，获取商品信息
							uni.request({
								url: 'http://localhost:8016/getProduct',
								method: 'GET',
								data: {
									id: id
								},
								header: {
									'content-type': 'application/json'
								},
								success: (res) => {
									if (res.data.code == 200) {
										const productInfo = res.data.result;
										// 使用转码后的图片Base64字符串
										productInfo.img = base64Image;
										resolve(productInfo);
									} else {
										reject('Failed to receive product info');
									}
								},
								fail: (err) => {
									reject('Request failed');
								}
							});
						}).catch(error => {
							reject('Image download and convert failed: ' + error);
						});
					});
				});
				Promise.all(requests).then(productInfos => {
					productInfos.forEach(productInfo => {
						this.categoryList.push({
							list: [{
								name: productInfo.name,
								descripe: productInfo.description,
								img: productInfo.img,
								price: productInfo.price + "￥",
								id: productInfo.id,
							}]
						});
					});
				}).catch(error => {
					console.error(error);
				});
			},
			downloadAndConvertImage(productId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: 'http://localhost:8016/file/productDownloadViaId2',
						method: 'GET',
						data: {
							productId: productId
						},
						responseType: 'arraybuffer', // 设置响应类型为数组缓冲区，以便接收二进制数据
						success: (res) => {
							if (res.statusCode === 200) {
								// 将二进制数据转换为Base64字符串
								const base64Image = wx.arrayBufferToBase64(res.data);
								resolve('data:image/jpeg;base64,' + base64Image);
							} else {
								// 如果没有找到图片，返回默认图片路径
								resolve('@/static/img/icon/home_icon_refresh');
							}
						},
						fail: (err) => {
							// 如果请求失败，返回默认图片路径
							resolve('@/static/img/icon/home_icon_refresh');
						}
					});
				});
			},
		},
		onLoad() {
			const value = uni.getStorageSync("userinfo")
			this.userID = value.id
			console.log("用户ID：" + this.userID)
			this.sendUserIdToBackend(this.userID);
		}
	}
</script>

























<style lang="scss">
	@keyframes showPopup {
		0% {
			opacity: 0;
		}

		100% {
			opacity: 1;
		}
	}

	@keyframes hidePopup {
		0% {
			opacity: 1;
		}

		100% {
			opacity: 0;
		}
	}

	@keyframes showLayer {
		0% {
			transform: translateY(0);
		}

		100% {
			transform: translateY(-100%);
		}
	}

	@keyframes hideLayer {
		0% {
			transform: translateY(-100%);
		}

		100% {
			transform: translateY(0);
		}
	}

	.m-store-banner {
		min-height: 270upx;
		position: relative;
		padding-top: 20upx;
		background: #eee;
		// background:url("../../static/img/storebanner.png");
		background-size: 100% 200upx;
		background-repeat: no-repeat;

		.m-content {
			width: 690upx;
			position: relative;
			bottom: 10upx;
			left: 30upx;
			right: 30upx;
			margin-top: 30upx;
			background: #fff;
			border-radius: 15upx;
			box-shadow: 0upx 5upx 20upx rgba(0, 0, 0, 0.1);
			z-index: 100;

			.m-message {
				padding: 20upx;
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				align-items: flex-end;

				.m-img {
					flex: 0 0 135upx;
					height: 135upx;
					// background:url("../../static/img/storebanner.png");
				}

				.m-body {
					flex: 1;
					// padding: 20upx;
					padding-left: 10upx;
					padding-bottom: 0;

					.m-title {
						font-size: 32upx;
						color: #333333
					}

					.m-text {
						font-size: 20upx;
						color: #999999;
						line-height: 38upx;
						margin-top: 10upx;
					}

					.m-time {
						font-size: 20upx;
						color: #999999;
						margin-top: 10upx;
					}
				}

				.m-phone {
					flex: 0 0 40upx;
					text-align: right
				}
			}
		}
	}

	.back-button {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		display: flex;
		height: 7%;
		align-items: center;
		background-color: #fda085;
		border: none;
		color: #323232;
		padding: 10px 20px;
		cursor: pointer;
		font-size: 14px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
		z-index: 1000;
	}

	.popup {
		position: fixed;
		top: 0;
		width: 100%;
		height: 100%;
		z-index: 300;
		display: none;

		.mask {
			position: fixed;
			top: 0;
			width: 100%;
			height: 100%;
			z-index: 21;
			background-color: rgba(0, 0, 0, 0.6);
		}

		.layer {
			position: fixed;
			z-index: 22;
			bottom: -70%;
			width: 100%;
			padding: 0;
			height: 70%;
			border-radius: 20upx 20upx 0 0;
			background-color: #fff;
			display: flex;
			flex-wrap: wrap;
			align-content: space-between;

			// 购物车
			.m-shopcar-box {
				width: 100%;

				// padding: 30upx 0;
				.m-header {
					padding: 30upx 30upx;
					display: flex;
					flex-direction: row;
					justify-content: space-between;

					.m-line {
						display: flex;
						color: #333333;
						font-size: 30upx;

						.m-light {
							color: #333333;
							font-size: 22upx;
							padding-left: 10upx;
						}
					}

					.m-clear-car {
						color: #333333;
						font-size: 22upx;
					}
				}

				.m-shopcar-item {
					margin-left: 30upx;
					margin-right: 30upx;
					display: flex;
					flex-direction: row;
					justify-content: space-between;
					align-items: center;
					border-top: 1px solid #eee;
					padding: 20upx;

					&:last-of-type {
						border-bottom: 1px solid #eee;
					}

					.m-title {
						color: #4c4c4c;
						font-size: 26upx;
					}

					.m-price {
						color: #ff582b;
						font-size: 26upx;
					}

					.m-controne {
						// flex: 
					}
				}
			}

			.btn {
				width: 100%;
				height: 100upx;

				.button {
					width: 100%;
					height: 80upx;
					border-radius: 40upx;
					color: #fff;
					display: flex;
					align-items: center;
					justify-content: center;
					background-color: #f47952;
					font-size: 28upx;

				}
			}
		}

		&.show {
			display: block;

			.mask {
				animation: showPopup 0.2s linear both;
			}

			.layer {
				animation: showLayer 0.2s linear both;
			}
		}

		&.hide {
			display: block;

			.mask {
				animation: hidePopup 0.2s linear both;
			}

			.layer {
				animation: hideLayer 0.2s linear both;
			}
		}

		&.none {
			display: none;
		}

		&.service {
			.row {
				margin: 30upx 0;

				.title {
					font-size: 30upx;
					margin: 10upx 0;
				}

				.description {
					font-size: 28upx;
					color: #999;
				}
			}
		}

		&.spec {
			.title {
				font-size: 30upx;
				margin: 30upx 0;
			}

			.sp {
				display: flex;

				view {
					font-size: 28upx;
					padding: 5upx 20upx;
					border-radius: 8upx;
					margin: 0 30upx 20upx 0;
					background-color: #f6f6f6;

					&.on {
						padding: 3upx 18upx;
						border: solid 1upx #f47925;
					}
				}
			}

			.length {
				margin-top: 30upx;
				border-top: solid 1upx #aaa;
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding-top: 20upx;

				.text {
					font-size: 30upx;
				}

				.number {
					display: flex;
					justify-content: center;
					align-items: center;

					.input {
						width: 80upx;
						height: 60upx;
						margin: 0 10upx;
						background-color: #f3f3f3;
						display: flex;
						justify-content: center;
						align-items: center;
						text-align: center;

						input {
							width: 80upx;
							height: 60upx;
							display: flex;
							justify-content: center;
							align-items: center;
							text-align: center;
							font-size: 26upx;
						}
					}

					.sub,
					.add {
						width: 60upx;
						height: 60upx;
						background-color: #f3f3f3;
						border-radius: 5upx;

						.icon {
							font-size: 30upx;
							width: 60upx;
							height: 60upx;
							display: flex;
							justify-content: center;
							align-items: center;

						}
					}
				}
			}

		}
	}

	.activite {
		color: #04c9c3;
	}

	.content {
		height: 700px;
		background-color: #ffffff;
		color: #000;
		/* 修改文字颜色为黑色 */
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
		color: #CCCCCC;
	}
	.kongbai{
		margin-top: 60px;
	}

	.scroll-view-item_H {
		display: inline-block;
		width: 50%;
		/* 修改每个选项宽度为50% */
		height: 50rpx;
		line-height: 50rpx;
		text-align: center;
		padding: 10px 0;
		font-size: 32rpx;
	}

	.activite {
		color: #04c9c3;
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
	}

	.scroll-view-item_H {
		display: inline-block;
		width: 50%;
		height: 50rpx;
		line-height: 50rpx;
		text-align: center;
		padding: 10px 0;
		font-size: 32rpx;
	}

	.seeker-list {
		padding: 20px;
		background-color: #f7f7f7; // 设置一个轻柔的背景色以区分内容区域
	}

	.category {
		margin-bottom: 15px; // 每个项目之间的间距
		background-color: #ffffff; // 背景颜色为白色增加清晰度
		border-radius: 8px; // 圆角设计
		box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); // 轻微的阴影增加立体感
		padding: 15px; // 内部填充
		transition: all 0.3s ease; // 过渡效果使得鼠标悬浮变化平滑

		&:hover {
			box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2); // 鼠标悬浮时增加阴影
		}
	}

	.product-info {
		display: flex; // 使用flex布局
		flex-direction: column; // 元素垂直排列
	}

	text {
		font-size: 16px; // 文本大小
		color: #333; // 文本颜色
		line-height: 1.5; // 行高增加可读性

		&::before {
			content: attr(data-label);
			font-weight: bold; // 标签加粗
			margin-right: 5px; // 标签和内容之间的间距
		}
	}



	button {
		padding: 10px 20px; // 按钮内边距
		font-size: 14px; // 文字大小
		color: #fff; // 文字颜色
		background-color: #4CAF50; // 背景颜色
		border: none; // 去除边框
		border-radius: 4px; // 圆角边框
		cursor: pointer; // 鼠标指针变成手形
		transition: background-color 0.3s ease; // 平滑过渡效果

		&:hover {
			background-color: #45a049; // 鼠标悬停时变色
		}
	}

	button:nth-of-type(2) {
		background-color: #f44336; // 第二个按钮颜色

		&:hover {
			background-color: #dc2836; // 鼠标悬停时变色
		}
	}
</style>