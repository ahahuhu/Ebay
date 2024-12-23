<template name="drift">
	<view>
		<view class="xd-drift">
			<image style="width: 100%; height: 100%; " src="../../static/xiaodiu-drift/drift_home.png"></image>
			<image class="drift-plp" :style="{top:plptop+'px'}" src="../../static/xiaodiu-drift/drift_plp.png"></image>
			<view class="drift-bo1" :style="{left:(btleft-5)+'px'}">
				<image v-for="one in bolist1" class="drift-mbo" src="../../static/xiaodiu-drift/drift_mbo1.png"></image>

			</view>
			<view class="drift-bo2" :style="{left:btleft+'px'}">
				<image v-for="one in bolist1" class="drift-mbo" src="../../static/xiaodiu-drift/drift_mbo2.png"></image>

			</view>
			<view class="drift-btv">
				<image style="width: 31px; height: 31px;  " src="../../static/xiaodiu-drift/drift_bt.png"></image>
				<text @click="addbook()">加入书籍（点击书籍出库）</text>
			</view>
		</view>
		<u-grid :col="3" :border="false">
			<u-grid-item v-for="(book, index) in books" :key="index" @click="onBookClick(book)">
				<view class="book-item">
					<image class="book-cover" :src="book.bookimage" mode="aspectFill"></image>
					<text class="book-title">{{ book.bookname }}</text>
				</view>
			</u-grid-item>
		</u-grid>
	</view>
</template>

<script>
	export default {
		name: "drift",
		//属性
		data() {
			return {
				btleft: 0,
				btleft1: 0,
				bolist1: 30,
				windowWidth: 0,
				plptop: 55,
				plpadd: 1,
				tempid: 1,
				books: [{
						id: 1,
						bookname: '高等数学',
						bookimage: '/static/book.jpg'
					},
					{
						id: 1,
						bookname: '计算机组成原理',
						bookimage: '/static/book.jpg'
					},
					{
						id: 1,
						bookname: '大学物理',
						bookimage: '/static/book.jpg'
					},
					{
						id: 1,
						bookname: '机器学习',
						bookimage: '/static/book.jpg'
					},
				],
			}
		},
		props: {
			plpspeed: {
				type: Number,
				default: 1,
			},
			bospeed: {
				type: Number,
				default: 1,
			},
			userid: {
				type: Number,
				default: 1,
			}

		},
		//组件生命周期
		created: function(e) {
			var that = this;
			uni.getSystemInfo({
				success: function(res) {
					that.windowWidth = res.windowWidth;
					this.btleft = 100 - res.windowWidth;
				}
			});
			this.beijing();
			this.fetchBooks(); // 在组件创建时获取书籍数据  
		},
		methods: {
			fetchBooks() {
				uni.request({
					url: 'http://localhost:8016/book/list', // 替换为你的书籍数据API地址  
					method: 'GET', // 根据你的API调整请求方法  
					success: (res) => {
						if (res.data && res.data.code === 200) {
							// 假设服务器返回的数据中包含一个books数组  
							this.books = res.data.result; // 将获取到的书籍数据赋值给books数组  

						}
					},
					fail: (err) => {
						console.error('获取书籍数据失败:', err);
					},
				});
			},
			showBt: function(obj) {},
			beijing: function() {
				this.btleft += this.bospeed;
				this.plptop = this.plptop + this.plpadd;
				if (this.plptop <= 55) {
					this.plpadd = this.plpspeed;
				}
				if (this.plptop >= 60) {
					this.plpadd = -this.plpspeed;
				}
				if (this.btleft + 100 >= 0) this.btleft = 100 - this.windowWidth;
				var that = this;
				setTimeout(function() {
					that.beijing();
				}, 100);
			},
			onBookClick(book) {
				const value = uni.getStorageSync("userinfo")
				if (!value) {
					uni.showToast({
						title: '未登录！',
						icon: 'none',
						duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
					});
					return
				}
				uni.showModal({
					title: '是否要取出这本书？',
					// content: '',  
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: '书籍取出成功!',
								icon: 'success',
							});
							uni.request({
								url: 'http://localhost:8016/book/delete?id=' + book.id,
								method: 'GET',
								success: (checkoutRes) => {
									// 处理取出书籍成功的逻辑  
									console.log('书籍取出成功:', checkoutRes.data);
									this.fetchBooks();
								},
								fail: (err) => {
									console.error('取出书籍失败:', err);
								}
							});
						} else if (res.cancel) {
							// 用户点击取消  
							uni.showToast({
								title: '已取消取出书籍',
								icon: 'none',
							});
						}
					}
				});
			},
			addbook() {
				const value = uni.getStorageSync("userinfo")
				if (!value) {
					uni.showToast({
						title: '未登录！',
						icon: 'none',
						duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
					});
					return
				}
				uni.showModal({
					title: '加入书籍',
					editable: true, //是否显示输入框
					placeholderText: '请输入书籍名', //输入框提示内容
					success: (res) => {
						console.log("加入的书籍:" + res.content)
						if (res.confirm) {

							uni.request({
								url: 'http://localhost:8016/book/insert',
								method: 'POST',
								data: {
									bookname: res.content,
									userid: this.userid,
								},
								success: (res) => {
									if (res.data.code == 200) {
										console.log("插入书籍成功:" + res.data.result)
										this.uploadBookCover(res.data.result.id);
									}
								}
							})
						}
					}
				});
			},
			uploadBookCover(id) {
				console.log("书籍id:" + id)
				uni.showToast({
					title: '请选择一张图片',
					icon: 'none',
					duration: 2000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
				});
				uni.chooseImage({
					// ... 选择图片逻辑 ...  
					success: (chooseImageRes) => {
						const tempFilePath = chooseImageRes.tempFilePaths[0]; // 假设用户只选择了一张图片  
						uni.uploadFile({
							url: 'http://localhost:8016/book/upload?bookId=' +
							id, // 注意：这个 URL 可能需要根据你的后端接口进行调整  
							filePath: tempFilePath,
							name: 'file', // 后端接收文件的参数名，默认是 file  
							success: (uploadFileRes) => {
								// 处理上传成功的情况  
								console.log('上传图片:', uploadFileRes.data);
								uni.showToast({
									title: '上传书籍图片成功,感谢您的参与！',
									icon: 'success',
								});
								this.fetchBooks();
							},
							fail: (err) => {
								// 处理上传失败的情况  
								console.error('上传失败:', err);
								uni.showToast({
									title: '上传书籍图片失败',
									icon: 'none',
								});
							},
						});
					},
				});
			},
		}
	}
</script>


<style>
	.xd-drift {

		width: 100%;
		height: 250px;
	}

	.drift-plp {
		width: 26px;
		height: 37px;
		position: fixed;
		top: 59px;
		left: 88px;

	}

	.drift-bo1 {
		position: fixed;
		top: 70px;
		left: 0;
		width: 200%;
		height: 25px;
		overflow: hidden
	}

	.drift-bo2 {
		position: fixed;
		top: 80px;
		left: 0;
		width: 200%;
		height: 25px;
		overflow: hidden
	}

	.drift-mbo {
		width: 44px;
		height: 22px;

	}

	.drift-btv {
		position: relative;
		font-size: 14px;
		bottom: 50px;
		left: 10px;
	}

	.drift-btv {
		line-height: 31px;
	}

	.drift-btv image {
		vertical-align: middle;
		justify-content: center;
		align-items: center;
	}

	.book-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 5px;

	}

	.book-cover {
		width: 100px;
		height: 140px;
		border-radius: 5px;
	}

	.book-title {
		margin-top: 10px;
		text-align: center;
	}
</style>