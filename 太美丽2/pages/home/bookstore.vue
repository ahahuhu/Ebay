<template>
	<xiaodiu-drift :userid="userid"></xiaodiu-drift>
</template>

<script>
     import xiaodiudrift from '@/components/xiaodiu-drift/xiaodiu-drift.vue'; 
	export default {
		components:{
			xiaodiudrift,
		},
		data() {
			return {
				userid:1,
			}
		},
		onShow() {
			const value=uni.getStorageSync("userinfo")
			this.userid=value.id 
			console.log("当前用户:"+this.userid)
		},
		methods: {
			
		}
	}
</script>

<style>

</style>
