<template>
  <MusicPlayer />
  <PolicyModal />
  <router-view />
</template>

<script setup>
import PolicyModal from './components/PolicyModal.vue'
import MusicPlayer from './components/MusicPlayer.vue'
</script>
