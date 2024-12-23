<template>
	<view class="container">
		<!-- 返回按钮 -->
		<button class="back-button" @click="goBack">
			<uni-icons type="left" size="25" style="position: relative;"></uni-icons>
			<u-icon name="arrow-left"></u-icon>
			返回
		</button>

		<!-- 图片展示区域 -->
		<swiper class="image-swiper" indicator-dots="true" autoplay="true" interval="3000" duration="500">
			<swiper-item v-for="(image, index) in images" :key="index">
				<image :src="image" class="product-image"></image>
			</swiper-item>
		</swiper>

		<!-- 商品信息区域 -->
		<view class="product-info">
			<text class="product-title">{{goods.name}}</text>
			<!-- 商品描述 -->
			<view class="product-description">
				<text>{{goods.description}}</text>
			</view>
		</view>

    <view class="product-info-row">  
        <text class="product-seller">{{goods.userName}}</text>  
        <text class="product-price">{{'¥'+goods.price}}</text>  
    </view>  
		<view class="footer">
			<button class="icon" @click="HandleCollect">
				<view v-if="isCollected">
					<uni-icons type="star-filled" size="20" style="position: relative;"></uni-icons>
				</view>
				<view v-else>
					<uni-icons type="star" size="20" style="position: relative;"></uni-icons>
				</view>
				收藏
			</button>
			<!-- ⭐ ?? -->
			<button class="icon" @click="communicate">
				<uni-icons type="chatbubble" size="20" style="position: relative;"></uni-icons>
				我想要
				</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				goods: {
					id: 153, //商品ID
					userId: 12, //该商品所属用户的ID
					userName: '张三', //卖家名字
					name: "高等数学",
					category: null,
					description: '好',
					price: '100',
					deliveryArea: '梅二',
				},

				images: [],
				images_list: [],
				uPic: "",
				isCollected: false,
				my:{  //本地已登录用户的ID
	                id:1,
					name:'',
					avatar:''
				} , 
				your: { //卖家的信息，用于跳转到聊天界面
					id: 1,
					name: '',
					avater: ''
				}
			};
		},
		methods: {
			HandleCollect() {
				const value=uni.getStorageSync("userinfo")
				 if(!value){
					 uni.showToast({
					 	title: '未登录！',
					 	icon: 'none',
					 	duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
					 });
					 return
				 }
				// 向后端发送请求
				console.log(this.isCollected);
				if (!this.isCollected) { //商品还没有被收藏
					uni.request({
						url: 'http://localhost:8016/setFavorite',
						data: {
							//商品ID
							id: this.goods.id,
							//用户ID
							userID: this.my.id
						},
						method: "GET",
						success: (res) => {
							console.log("res")
							if (res.data.code == 200) {
								console.log("收藏成功")
								this.isCollected = true;
								// 显示已收藏的提示框
								this.$u.toast("收藏成功！");
							} else if (res.data.code == 202) {
								console.log("该商品您已收藏！")
								// 显示已收藏的提示框
								this.$u.toast("该商品您已收藏！");
							} else {
								console.log("收藏失败，请重试")
							}
						},
						fail: (res) => {
							console.log(res);
							console.log("请求失败")
						}
					})
				} else { //商品已经被收藏
					uni.request({
						url: 'http://localhost:8016/deleteFavorite',
						data: {
							//商品ID
							id: this.goods.id,
							//用户ID
							userID: this.my.id
						},
						method: "GET",
						success: (res) => {
							console.log("res")
							if (res.data.code == 200) {
								console.log("取消收藏成功")
								this.isCollected = false;
								// 显示已收藏的提示框
								this.$u.toast("取消收藏成功！");
							} else {
								console.log("取消收藏失败，请重试")
							}
						},
						fail: (res) => {
							console.log(res);
							console.log("请求失败")
						}
					})
				}

			},
			communicate() {
				const value=uni.getStorageSync("userinfo")
				 if(!value){
					 uni.showToast({
					 	title: '未登录！',
					 	icon: 'none',
					 	duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
					 });
					 return
				 }
				console.log("与卖家交流")
				uni.navigateTo({
					url: `/pages/message/chattingbox?id=${this.your.id}&name=${this.your.name}&avatar=${this.your.avater}`
				})
			},
			goBack() {
				console.log("返回上一级页面");
				uni.navigateBack();
			},

			loadProductImage() {
				console.log(this.images_list.length);
				//根据地址获取能显示的URl
				for (let i = 0; i < this.images_list.length; i++) {
					console.log("第" + i + "次请求");
					console.log(this.images_list[i]);
					uni.request({
						url: 'http://localhost:8016/file/imagesProductDownload',
						data: {
							URL: this.images_list[i]
						},
						method: "GET",
						responseType: "arraybuffer",
						success: (res) => {
							console.log(res);

							this.uPic = btoa(new Uint8Array(res.data).reduce((data, byte) => data + String
								.fromCharCode(byte), '')), 'base64';
							this.uPic = 'data:image/png;base64,' + this.uPic;
							this.images.push(this.uPic);
							console.log("请求商品图片的url成功")
						}
					})
				}
			},

		},
		//接受上个页面传递的productId参数
		onLoad(option) { //option为object类型，会序列化上个页面传递的参数
			console.log(option); //打印出上个页面传递的参数
			this.goods.id = option.id;
			console.log(this.goods.id);
		},

		onShow() {
			console.log("展示")
			this.images = [];
			//获取本地缓存中已登录用户的ID！！！！！
			const value = uni.getStorageSync("userinfo")
			this.my.id = value.id
			this.my.name=value.nickname
			this.my.avatar=value.avatarName
			//获取前一个页面传递的商品ID
			//待填写

			//获取商品信息
			uni.request({
				url: 'http://localhost:8016/getProduct',
				data: {
					id: this.goods.id
				},
				method: "GET",
				success: (res) => {
					console.log(res);
					if (res.data.code == 200) {
						console.log(res.data.result);
						this.goods.userId = res.data.result.userId;
						this.goods.userName = res.data.result.userName;
						this.goods.name = res.data.result.name;
						this.goods.category = res.data.result.category;
						this.goods.description = res.data.result.description;
						this.goods.price = res.data.result.price;
						this.goods.deliveryArea = res.data.result.deliveryArea;
						this.your.id=this.goods.userId;
						this.your.name=this.goods.userName;
						console.log(this.goods);

			//获取商品图片的地址
			// uni.request({
			// 	url: 'http://localhost:8016/file/imagesURL',
			// 	data: {
			// 		productId: this.goods.id
			// 	},
			// 	method: "GET",
			// 	success: (res) => {
			// 		console.log(res);
			// 		if (res.data.code == 200) {
			// 			this.images_list = res.data.result;
			// 			console.log("请求商品图片的地址成功")
			// 			//请求成功后，继续调用第二个请求，根据地址获取能显示的URl
			// 			this.loadProductImage();
			// 		} else {
			// 			console.log("请求商品图片的地址失败")
			// 		}
			// 	}
			// })
			//直接获取商品图片
			uni.request({
				url: 'http://localhost:8016/file/imagesURLStragt',
				data: {
					productId: this.goods.id
				},
				method: "GET",
				success: (res) => {
					console.log(res);
					if (res.data.code == 200) {
						console.log(res.data.result)
						for (let i = 0; i < res.data.result.length; i++) {
							this.images.push(res.data.result[i].url);
						}
						console.log("请求商品图片成功")

					} else {
						console.log("请求商品图片失败")
					}
				},
				fail: (res) => {
					console.log("请求失败")
				}
			})
			//获取商品是否被收藏
			console.log(this.goods.id, this.goods.userId, this.userId);
			uni.request({
				url: 'http://localhost:8016/isFavorited',
				method:'GET',
				data: {
					//商品ID
					productID: this.goods.id,
					//用户ID
					userID: this.my.id
				},
				method: "GET",
				success: (res) => {
					console.log(res)
					if (res.data.code == 200) {
						console.log("查询是否被收藏成功")
						this.isCollected = res.data.result == 0;
						console.log(this.isCollected)
					} else {
						console.log("查询是否被收藏失败")
					}
				},
				fail: (res) => {
					console.log(res);
					console.log("请求失败")
				}
			})
						console.log("请求商品信息成功")

					} else {
						console.log("请求商品信息失败")
					}
				},
				fail: (res) => {
					console.log(res);
					console.log("请求失败")
				}
			})
             //获取对方图片，用于对话
			uni.request({
				url:'http://localhost:8016/doSearch',
				method:'POST',
				data:{
					id:this.your.id
				},
				success: (res) => {
					if(res.data.code==200){
						console.log("请求对方头像成功")
						this.your.avatar=res.data.result.avatarName
                        console.log(this.your.avatar)
					}
				},fail: (res) => {
					console.log("请求对方头像失败")
				}
			})
		}
	};
</script>

<style scoped>
	html,
	body,
	.container {
		height: 800px;
		margin: 0;
		padding: 0;
		font-family: Arial, sans-serif;
	}

	.container {
		display: flex;
		flex-direction: column;
		background-color: #f9f9f9;
		position: relative;
		padding-top: 50px;
		/* 预留给返回按钮的位置 */
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

	.back-button uni-icons {
		margin-right: 10px;
	}

	.image-swiper {
		height: 400px;
		width: 100%;
		margin-top: 5px;
		border-radius: 10px;
		overflow: hidden;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	}

	.product-image {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}

	.product-info {
		margin-top: 20px;
		text-align: center;
	}
	.product-info2 {
		margin-right: 50px;
		text-align: right;
	}
	.product-info-row {  
	    display: flex;  
	    justify-content: space-between;  
	    align-items: center;  
	    margin-top: 20px; /* 或者你需要的任何间距 */  
	}  
	  
	.product-seller {  
	    font-size: 20px;  
	    font-weight: bold;  
		margin-left: 40px;
	    /* margin-left: 50px; 移除或调整这个边距 */  
	}  
	  
	.product-price {  
	    color: #e53935;  
	    font-size: 20px;  
		margin-right: 50px;
	    /* margin-top: 5px; 移除这个上边距，因为它现在与seller在同一行 */  
	}
	.product-title {
		font-size: 24px;
		font-weight: bold;
		color: #333;
	}

	.product-description {
		padding: 20px;
		font-size: 16px;
		line-height: 1.5;
		text-align: center;
		color: #666;
	}
	.footer {
		display: flex;
		justify-content: space-around;
		align-items: center;
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		max-width: 600px;
		background-color: #fff;
		padding: 20px;
		box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
	}

	.footer .icon {
		display: flex;
		background-color: #ffabab;
		border: none;
		color: #323232;
		padding: 10px 20px;
		border-radius: 20px;
		cursor: pointer;
		font-size: 14px;
		align-items: center;
	}

	.footer .icon:active {
		background-color: #f6d365;
	}
</style>