<template>
	<view>
		<!-- 返回按钮 -->
		<button class="back-button" @click="goBack">
			<uni-icons type="left" size="25" style="position: relative;"></uni-icons>
			<u-icon name="arrow-left"></u-icon>
			返回
		</button>
		<view class="kongbai"></view>

		<!-- 顶部导航栏 -->
		<scroll-view class="scroll-view_H" scroll-x="true" @scroll="scroll">
			<view class="scroll-view-item_H" v-for="(tab, index) in tabBars" :key="tab.id" :id="tab.id"
				:class="navIndex==index ? 'activite' : ''" @tap="checkIndex(index)">
				{{ tab.name }}
			</view>
		</scroll-view>

		<!-- 商品分类信息列表 -->
		<view class="category-list" v-if="navIndex == 0">
			<view class="category" v-for="(category, index) in categoryList" :key="category.id">
				<view class="list">
					<view class="box" v-for="(box, i) in category.list" :key="i">
						<!-- 商品列表 -->
						<m-store-pro @touchOnGoods="touchOnGoods" :rowData="box"></m-store-pro>
					</view>
				</view>
			</view>
		</view>

		<!-- 求购信息列表 -->
		<view class="seeker-list" v-else-if="navIndex == 1">
			<view class="category" v-for="(item, index) in categoryList" :key="index">
				<view class="product-info">
					<view class="product-name">名称：{{ item.name }}</view>
					<view class="product-description">描述：{{ item.description }}</view>
					<view class="product-price">价格：{{ item.price }}</view>
					<view class="product-releaseTime">发布时间：{{ item.releaseTime }}</view>
					<!-- <text>发布时间：{{ item.seekerId }}</text> -->
					<!-- 添加按钮 -->
					<view class="buttons">
						<!-- <button @click="xiugaiqiugou(item.seekerId)">修改求购</button> -->
						<button @click="xiugaiqiugou(item)">修改</button>
						<button @click="shanchuqiugou(item.seekerId)">删除</button>
					</view>
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	import mFooterCar from '@/components/m-footer-car' // 底部
	import mStorePro from '@/components/m-store-pro-onsale' // 商品列表
	import uniNumberBox from "@/components/uni-number-box/uni-number-box.vue" // 加减数字

	export default {
		components: {
			mStorePro,
			uniNumberBox,
			mFooterCar
		},
		data() {
			return {
				scrollTop: 0,
				navIndex: 0,
				tabBars: [{
						name: '我的在售',
						id: 'purchased'
					},
					{
						name: '我的需求',
						id: 'sold'
					}
				],
				categoryList: [],
				userID: '12',
				props: {
					item: {
						type: Object,
						default: () => ({}),
					}
				},
				// name: "",
				// descripe: "",
				// img: "",
				// price: "",
				// id: "",
				// seekerId:"",
			}
		},
		onLoad() {
			const value=uni.getStorageSync("userinfo")
			this.userID=value.id
			console.log("当前用户："+this.userID)
			this.sendUserIdToBackend(); // 默认加载“我的在售”
		},
		methods: {
			goBack() {
				console.log("返回上一级页面");
				uni.switchTab({
					url: '/pages/my/my'
				})
			},
			shanchuqiugou(id) {
				const userID = 10; // 用户ID，这里使用的是固定值，实际应用中应该从父组件或Vuex获取
				uni.request({
					url: 'http://localhost:8016/closeSeeker',
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
							// uni.reLaunch({
							// 	url: '/pages/home/home' // 替换为当前页面的路径
							// });
							this.fetchSeekerInfo();
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
			},
			// xiugaiqiugou() {

			// 	console.log("1"); 
			// 	// const seekerIddd = this.item.seekerId;
			// 	// uni.navigateTo({
			// 	// 	url: `/pages/index/GoodsInfo/GoodsInfo?seekerIddd=${seekerIddd}`
			// 	// });
			// 	uni.navigateTo({
			// 		url: '/pages/index/GoodsInfo/GoodsInfo'
			// 	});
			// },
			xiugaiqiugou(item) {
				if (item && item.seekerId) {
					const seekerIddd = item.seekerId;
					console.log(seekerIddd); // 输出seekerIddd以确保它被正确赋值
					// 跳转到index页面，并传递seekerIddd作为参数
					uni.navigateTo({
						url: `/pages/my/update_want_info?seekerId=${seekerIddd}`
					});
				} else {
					console.error('Item or seekerId is undefined.');
				}
			},

			checkIndex(index) {
				this.navIndex = index;
				this.categoryList = []; // 清空列表
				if (index == 0) { // 如果是“我的在售”
					this.sendUserIdToBackend(); // 获取商品列表
				} else if (index == 1) { // 如果是“我的需求中”
					this.fetchSeekerInfo(); // 获取求购信息
				}
			},
			sendUserIdToBackend() {
				console.log(this.userID)
				uni.request({
					url: 'http://localhost:8016/userProductSaleOnlyID',
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

			fetchSeekerInfo() {
				uni.request({
					url: `http://localhost:8016/userSeeker`,
					method: 'GET',
					data: {
						userID: this.userID
					},
					header: {
						'content-type': 'application/json'
					},
					success: (res) => {
						if (res.data.code == 200) {
							this.categoryList = res.data.result.map(item => ({
								name: item.name,
								description: item.description,
								price: `￥${item.price}`,
								releaseTime: new Date(item.releaseTime).toLocaleString(),
								img: item.pic,
								seekerId: item.seekerId
							}));
						} else {
							console.error('Failed to load seeker info:', res.msg);
						}
					},
					fail: (err) => {
						console.error('Request failed:', err);
					}
				});
			},

		},
	}
</script>

<style lang="scss">
	// 定义橙色系颜色变量
	$orange: #ff9500;
	$dark-orange: #d8700c;
	$light-orange: #ffe0c4;

	// 顶部导航栏和内容通用样式
	.activite {
		color: $orange;
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

	.kongbai {
		margin-top: 60px;
	}

	.content {
		height: 700px;
		background-color: #ffffff;
		color: $dark-orange;
	}

	.scroll-view_H {
		margin-bottom: 10px;
		white-space: nowrap;
		width: 100%;
		color: #CCCCCC;
	}

	.scroll-view-item_H {
		display: inline-block;
		width: 33.3333%;
		height: 50rpx;
		line-height: 50rpx;
		text-align: center;
		padding: 10px 0;
		font-size: 32rpx;
	}

	// 求购信息列表样式
	.seeker-list {
		padding: 20px;
		background-color: $light-orange;
	}

	.category {
		margin-bottom: 15px;
		background-color: #ffffff;
		border-radius: 8px;
		box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
		padding: 15px;
		transition: all 0.3s ease;

		&:hover {
			box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
		}
	}

	.product-info {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
	}

	// 为名称，描述，价格，发布时间设置不同的样式
	.product-name {
		font-size: 32upx; // 名称使用较大字体，与.m-title相同
		color: #4c4c4c; // 使用与.m-title相同的颜色
		margin-bottom: 10upx;
	}

	.product-description {
		font-size: 24upx; // 描述使用标准字体，与.m-descripe相同
		color: #999999; // 使用与.m-descripe相同的颜色
		margin-top: 10upx;
		margin-bottom: 10upx;
	}

	.product-price {
		font-size: 28upx; // 价格使用较大字体，与.m-price相同
		color: #ff582b; // 使用与.m-price相同的颜色
		font-weight: bold; // 加粗，与.m-price相同
		margin-top: 5upx;
		margin-bottom: 10upx;
	}

	// 注意：.product-release-time 的样式参考的是 .m-distance，但是您没有提供 .m-distance 的样式。
	// 假设 .m-distance 的样式与 .m-title 相同，以下是相应的修改：
	.product-release-time {
		font-size: 20upx; // 发布时间使用较小字体，假设与.m-title相同
		color: #4c4c4c; // 使用与.m-title相同的颜色
		margin-top: 5upx;
	}

	// 按钮样式
	.buttons {
		height: 30px;
		display: flex;
		justify-content: space-between;
		margin-top: 15px;
		margin-left: 180px;
	}

	button {
		padding: 0px 8px; // 保持较小的按钮内边距
		font-size: 12px; // 保持较小的按钮文字大小
		color: #fff;
		background-color: $orange;
		border: none;
		border-radius: 4px;
		cursor: pointer;
		transition: background-color 0.3s ease;
		margin-left: 10px; // 在按钮之间添加一些间隔

		&:hover {
			background-color: $dark-orange;
		}
	}

	button:nth-of-type(2) {
		background-color: #f44336;

		&:hover {
			background-color: #dc2836;
		}
	}
</style>