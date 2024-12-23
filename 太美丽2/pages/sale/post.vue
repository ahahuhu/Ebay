<template>
	<view class="container">

		<view class="input-group">
			<text class="label-text">商品名称</text><br>
			</br><input class="input-field" placeholder="请输入商品名称" placeholder-style="color: #999; font-size: 16px;"
				v-model="goods.name" type="text" />
		</view>

		<view class="input-group">
			<text class="label-text">商品分类</text><br>
			</br>
			<picker mode="selector" :range="categories" @change="onCategoryChange">
				<view class="picker">
					{{ goods.category !== null ? categories[goods.category] : '请选择商品分类' }}
				</view>
			</picker>
		</view>

		<view class="input-group">
			<text class="label-text">商品描述</text><br>
			</br><textarea class="textarea-field" placeholder="描述一下商品的品牌、来源..." v-model="goods.description"
				@input="handleInput" />
		</view>

		<view class="input-group">
			<text class="label-text">图片上传</text>
			<u-upload ref="uUpload1" :action="action" :auto-upload="false" :max-size="5 * 1024 * 1024" max-count="6"
				:form-data="formData" :fileList.sync="fileList" @change="handleChange" @success="handleSuccess"
				@fail="handleFail">
			</u-upload>
		</view>

		<view class="input-group">
			<view class="price-container">
				<text class="label-text">价格（单位：元）
					<u-button class="price-recommend" @click="recom">价格推荐</u-button>
				</text>
			</view>
			<input class="input-field" placeholder="¥0.00" v-model="goods.price" type="number" />
		</view>

		<view class="input-group shipping-group">
			<text class="label-text">商品所在位置</text><br>
			</br><input class="location-label" placeholder="请输入商品所在校区和楼栋"
				placeholder-style="color: #999; font-size: 16px;" v-model="goods.deliveryArea" />
		</view>

		<button class="submit-button" @click="handleSubmit">发布</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {

				categories: ['书籍', '电子数码', '运动户外', '生活用品', '食品', '其他'],
				goods: {
					userId: 12,
					name: "",
					category: null,
					description: '',
					price: '',
					deliveryArea: '',
				},
				action: 'http://localhost:8016/file/uploadProductImage',
				formData: {
					'userId': 12,
					'productId': 37,
					'file': ""
				},
				filesArr: [],
				imageList: [], // 存储选择的图片路径列表
				fileList: [] // 存储选择的文件列表


			}
		},
		methods: {
			recom() {
				uni.navigateTo({
					url: '/pages/sale/price_recom'
				})
			},
			onCategoryChange(e) {
				this.goods.category = e.detail.value;
				console.log(this.goods.category);
			},
			handleInput(event) {
				this.goods.description = event.target.value;
			},
			handleSubmit() {
				//console.log(!this.goods.name,!this.categories[this.goods.category],!this.goods.description,!this.goods.price);
				if (!this.goods.name || !this.categories[this.goods.category] || !this.goods.description || !this.goods
					.price) {
					this.$u.toast("请填写完整信息");
					return; // 停止提交
				}
				uni.showToast({
					title: '发布成功',
					icon: 'success',
					duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
				});

				// 使用setTimeout延迟1秒后执行返回操作  
				setTimeout(() => {
					// 返回上一个页面，uni-app中通常使用uni.navigateBack()  
					uni.navigateBack();
				}, 1000); // 延迟时间为1000毫秒，即1秒  
			
		console.log('发布内容:', {
			userId: this.goods.userId,
			name: this.goods.name,
			category: this.goods.category,
			description: this.goods.description,
			price: this.goods.price,
			deliveryArea: this.goods.deliveryArea
		});
		uni.request({
			url: "http://localhost:8016/addProduct",
			data: this.goods,
			method: "POST",
			success: (res) => {
				console.log(res.data.code);
				console.log(res);
				if (res.data.code == 200) {
					//更新fromData的商品ID
					this.formData.productId = res.data.result.id;
					this.$nextTick(() => {
						if (this.$refs.uUpload1) {
							let files = this.$refs.uUpload1.lists;
							console.log(files);
							console.log(this.fileList);
							//更新formData的值
							this.formData.userId = this.goods.userId;
							this.formData.file = files;

							this.$refs.uUpload1.upload();
						} else {
							console.error('uUpload1 ref is undefined');
						}
					});
					console.log("发布成功")
				} else {
					console.log("发布失败，请重试")
				}
			}
		})


	},
	handleChange(fileList) {
			console.log('文件变化：', fileList);
			this.fileList = fileList;
		},
		handleSuccess(response, file) {
			console.log('上传成功：', response);
			uni.showToast({
				title: '上传成功',
				icon: 'success'
			});
		},
		handleFail(error, file) {
			console.error('上传失败：', error);
			uni.showToast({
				title: '上传失败',
				icon: 'none'
			});
		},

		radioChange(e) {
			console.log(e);
		},
		radioGroupChange(e) {
			console.log(e);
		}
	}
	}
</script>

<style>
	.container {
		background-image: linear-gradient(120deg, #f6d365 0%, #fda085 100%);
		padding: 20px;
		background-color: #f9f9f9;
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.header {
		width: 100%;
		padding: 20px 0;
		text-align: center;
		background-color: #1065a3;
		color: white;
		font-size: 24px;
		font-weight: bold;
		border-radius: 8px;
		margin-bottom: 20px;
	}

	.input-group {
		width: 100%;
		background-color: #fff;
		padding: 20px;
		border-radius: 10px;
		margin-bottom: 20px;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	}

	.label-text {
		font-size: 18px;
		margin-bottom: 10px;
		color: #333;
	}

	.input-field,
	.textarea-field,
	.picker,
	.location-label {
		width: 95%;
		padding: 15px;
		border: 1px solid #ddd;
		border-radius: 5px;
		font-size: 16px;
	}

	.textarea-field {
		min-height: 100px;
		resize: none;
	}

	.picker {
		background-color: #fff;
		color: #1065a3;
	}

	.radio-container {
		display: flex;
		flex-direction: column;
		gap: 10px;
	}

	.radio-item {
		margin-top: 10px;
		font-size: 16px;
		display: flex;
		align-items: center;
		gap: 5px;
	}

	.submit-button {
		width: 100%;
		padding: 15px;
		background-color: #555500;
		color: #fff;
		text-align: center;
		border-radius: 10px;
		font-size: 18px;
		font-weight: bold;
		margin-top: 20px;
		cursor: pointer;
	}

	.u-upload {
		margin-top: 10px;
	}

	.price-container {
		display: flex;
		align-items: center;
		/* 垂直居中 */
		justify-content: space-between;
		/* 价格和按钮之间有空隙 */
		width: 100%;
		/* 容器宽度占满 */
	}

	.price-recommend {
		/* 根据需要调整按钮样式 */
		background-color: #fcbbd0;
		/* 示例背景色 */
		color: white;
		/* 示例文字颜色 */
		padding: 10px 20px;
		/* 示例内边距 */
		border-radius: 5px;
		/* 示例边框圆角 */
		/* 如果需要按钮只占右半边，可以通过设置flex属性来控制，但通常不推荐这样做，因为屏幕宽度不同会影响布局效果。  
	   如果确实需要，可以考虑使用媒体查询来适应不同屏幕宽度。  
	   但在这里，我们让按钮自然占据剩余空间，通过space-between实现与文本之间的自然间隔。 */
	}
</style>