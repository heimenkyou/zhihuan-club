<script setup lang="ts">
// 首页组件逻辑
</script>

<template>
  <div class="home">
    <h1>欢迎来到招新留言板</h1>
    <p>这是一个基于Vue3 + Vite + TypeScript的留言板应用</p>
    <router-link to="/messages" class="btn">查看留言列表</router-link>
  </div>
</template>

<style scoped>
.home {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
  text-align: center;
}

h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 2rem;
}

.btn {
  display: inline-block;
  padding: 0.8rem 1.5rem;
  background-color: #42b983;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.btn:hover {
  background-color: #3aa876;
}
</style>