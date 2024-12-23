<template>
	<view>
		<view class="u-wrap">
			<view class="u-demo-wrap"><!-- 滚动画面 -->
				<view class="u-demo-area">
					<u-toast ref="uToast"></u-toast>
					<u-swiper :height="300" :list="list1" :title="title" :effect3d="effect3d"
						:indicator-pos="indicatorPos" :mode="mode" :interval="3000" @click="click"></u-swiper>
				</view>
			</view>
		</view>
		<u-search border-color="#ccddff" bg-color="#ffffff" placeholder="高等数学" margin=8px :clearabled="true"
			:show-action="true" action-text="搜索" :action-style="SearchButtonStyle" v-model="keyword"
			@custom="onSearch()"></u-search>
		<image src="/static/filmpics/bottle.png" class="recommendation-icon"@click="books"></image>  
		<view class="product-recommendation">
			商品推荐
		</view>
		<view><!-- 商品瀑布流 -->
			<u-waterfall v-if="!isLoading" v-model="products" ref="uWaterfall">
				<template v-slot:left="{leftList}">
					<view class="demo-warter" v-for="(item, index) in leftList" :key="index"
						@click="goToProductDetail(item)">
						<u-lazy-load threshold="-450" border-radius="10" :image="item.url" :index="index"></u-lazy-load>
						<view class="demo-title">
							{{item.name}}
						</view>
						<view class="demo-price">
							{{item.price}}元
						</view>
						<view class="demo-tag">
							<view class="demo-tag-owner">
								自营
							</view>
							<view class="demo-tag-text">
								放心购
							</view>
						</view>
						<view class="demo-shop">
							{{item.userName}}
						</view>
						<u-icon name="close-circle-fill" color="#fa3534" size="34" class="u-close"
							@click="remove(item.id)"></u-icon>
					</view>
				</template>
				<template v-slot:right="{rightList}">
					<view class="demo-warter" v-for="(item, index) in rightList" :key="index"
						@click="goToProductDetail(item)">
						<u-lazy-load threshold="-450" border-radius="10" :image="item.url" :index="index"></u-lazy-load>
						<view class="demo-title">
							{{item.name}}
						</view>
						<view class="demo-price">
							{{item.price}}元
						</view>
						<view class="demo-tag">
							<view class="demo-tag-owner">
								自营
							</view>
							<view class="demo-tag-text">
								放心购
							</view>
						</view>
						<view class="demo-shop">
							{{item.userName}}
						</view>
						<u-icon name="close-circle-fill" color="#fa3534" size="34" class="u-close"
							@click="remove(item.id)"></u-icon>
					</view>
				</template>
			</u-waterfall>
			<u-loadmore bg-color="rgb(240, 240, 240)" :status="loadStatus" @loadmore="addRandomData"></u-loadmore>
		</view>
		<u-tabbar :list="tabbar" :mid-button="true"></u-tabbar> <!-- 导航栏 -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//轮播图
				list1: [{
						image: '/static/filmpics/pic1.png',
						title: '易贝二手，流转美好！'
					},
					{
						image: '/static/filmpics/轮播图2.png',
						title: '校园易贝，让每一份物品，遇见它的新故事'
					},
					{
						image: '/static/filmpics/轮播图3.png',
						title: '易贝，连接你我他，二手好物，校园新宠！'
					},
					{
						image: '/static/filmpics/轮播图4.png',
						title: '易贝校园，青春流转，二手也精彩'
					},
					{
						image: '/static/filmpics/轮播图5.png',
						title: '绿色校园，易贝先行'
					}
				],
				title: true,
				mode: 'round',
				indicatorPos: 'bottomCenter',
				effect3d: false,
				//结束轮播图
				keyword: '', //接受输入内容
				SearchButtonStyle: {
					backgroundColor: '#ff5511', // 定义控件的背景颜色  
					// fontSize: '13px', // 定义控件的字体大小 
					color: '#ffffff',
					borderRadius: '8px', // 添加圆角
					// 你可以继续添加其他需要的样式属性  
				},
				loadStatus: 'loadmore',
				flowList: [], //接受商品数据
				products: [],
				isLoading: true, // 添加数据加载状态  
			}
		},
		computed: {},
		onLoad() {
			this.getproducts();
			this.tabbar = [{
					iconPath: "/static/images/shop(unselected).png",
					selectedIconPath: "/static/images/shop(selected).png",
					text: '首页',
					count: 0,
					isDot: false,
					customIcon: false,
					pagePath: "/pages/home/home",
				},
				{
					iconPath: "/static/images/category(unselected).png",
					selectedIconPath: "/static/images/category(selected).png",
					text: '分类',
					customIcon: false,
					pagePath: "/pages/category/category",
				},
				{
					iconPath: "/static/images/sale(unselected).png",
					selectedIconPath: "/static/images/sale(selected).png",
					text: '卖二手',
					midButton: true,
					customIcon: false,
					pagePath: "/pages/sale/sale",
				},
				{
					iconPath: "/static/images/message(unselected).png",
					selectedIconPath: "/static/images/message(selected).png",
					text: '消息',
					customIcon: false,
					pagePath: "/pages/message/message",
				},
				{
					iconPath: "/static/images/my(unselected).png",
					selectedIconPath: "/static/images/my(selected).png",
					text: '我的',
					customIcon: false,
					pagePath: "/pages/my/my",
				},
			]
		},
		methods: {
			async getproducts() {
				this.isLoading = true; // 开始加载数据  
				try {
					const res = await uni.request({
						url: 'http://localhost:8016/allProduct',
						method: 'GET',
						
					});
					this.products = res.data.result;
					this.isLoading = false; // 数据加载完成  
				} catch (error) {
					console.error('加载数据失败', error);
					this.isLoading = false; // 加载失败也设置为完成状态  
				}
			},
			remove(id) {
				this.$refs.uWaterfall.remove(id);
			},
			clear() {
				this.$refs.uWaterfall.clear();
			},
			
			//轮播图
			click(index) {
				this.$refs.uToast.show({
					title: `点击了第${index + 1}张图⽚`,
					type: 'success'
				})
			},
			//结束轮播图
			imageError() {},
			onSearch() {
				if (!this.isLoading) { // 检查数据是否已加载完成  
					uni.navigateTo({
						url: `/pages/home/search?keyword=${this.keyword}`
					});
				} else {
					uni.showToast({
						title: '数据正在加载中，请稍候...',
						icon: 'loading'
					});
				}
			},
			goToProductDetail(item) { // 点击商品后跳转到商品信息界面
				console.log(item)
				console.log("点击商品" + item.id)
				uni.navigateTo({
					url: `/pages/home/commodity?id=${item.id}`
				})
			},
			books(){
				console.log("书籍漂流瓶")
				uni.navigateTo({
					url:'/pages/home/bookstore'
				})
			}
		}
	}
</script>
<style>
	/* page不能写带scope的style标签中，否则无效 */
	page {
		background-color: rgb(240, 240, 240);
	}
</style>
<style scoped lang="scss">
	@import "@/common/demo.scss";
</style>