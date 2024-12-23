<template>
	<view class="m-store-store">
		<u-search border-color="#ccddff" bg-color="#ffffff" placeholder="高等数学" margin=8px :clearabled="true"
			:show-action="true" action-text="搜索" :action-style="SearchButtonStyle" v-model="keyword"
			@custom="onSearch()"></u-search>
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
</template>
<script>
	import mStorePro from '@/components/m-store-pro2' // 商品列表
	import uniNumberBox from "@/components/uni-number-box/uni-number-box.vue" // 加减数字
	export default {
		components: {
			mStorePro,
			uniNumberBox,
		},
		data() {
			return {
				specClass: '', //规格弹窗css类，控制开关动画
				showCategoryIndex: 0,
				keyword:'',
				categoryList: [],
				userID: '10',
				SearchButtonStyle: {
					backgroundColor: '#ff5511', // 定义控件的背景颜色  
					fontSize: '16px', // 定义控件的字体大小 
					color: '#ffffff',
					borderRadius: '8px', // 添加圆角
					// 你可以继续添加其他需要的样式属性
				},
				url:''
			}
		},
		onLoad(option) {
			this.keyword=option.keyword
			console.log("查询商品:"+this.keyword)
			this.onSearch()
		},
		methods: {
			onSearch(){
				console.log(this.keyword);
				uni.request({
					url:'http://localhost:8016/searchProductByName',
					data:{
						words:this.keyword
					},
					method:"GET",
					success: (res) => {
						if(res.data.code==200){
							const ids=[];
							for(let i=0;i<res.data.result.length;i++){
								ids.push(res.data.result[i].id);
							}
							console.log(ids)
							this.fetchProductInfo(ids);
							console.log(this.categoryList);
						}
					}
				})
			},
			// sendUserIdToBackend(userID) {
			// 	uni.request({
			// 		url: 'http://localhost:8016/allFavorite',
			// 		method: 'GET',
			// 		data: {
			// 			userID: this.userID
			// 		},
			// 		header: {
			// 			'content-type': 'application/json'
			// 		},
			// 		success: (res) => {
			// 			if (res.data.code == 200) {
			// 				// 假设后端返回的数据格式是 { productIds: ['id1', 'id2', 'id3'] }
			// 				const ids = res.data.result;
			// 				// 获取商品信息
			// 				this.fetchProductInfo(ids);
			// 				console.log(this.categoryList);
			// 				console.log(ids)
			// 			} else {
			// 				console.error('Failed to receive product IDs:', res);
			// 			}
			// 		},
			// 		fail: (err) => {
			// 			console.error('Request failed:', err);
			// 		}
			// 	});
			// },
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
								price: "￥"+productInfo.price,
								id: productInfo.id,
							}]
						});
					});
				}).catch(error => {
					console.error(error);
				});
				console.log(this.categoryList);
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
								//resolve(this.url=res.data);
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
	}
</script>
<style>

</style>
























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
</style>