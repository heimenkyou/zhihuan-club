<template>
  <div class="resources-page min-h-screen bg-gray-50">
    <CommonNavbar />
    <main class="mx-auto mt-14 max-w-4xl px-4 py-8 sm:px-6 lg:px-8">
      <article class="rounded-2xl bg-white p-6 shadow-sm sm:p-10">
        <MdPreview id="newcomer-guide" :model-value="guideSource" />
      </article>
    </main>
    <CommonFooter />
  </div>
</template>

<script setup>
import { MdPreview } from "md-editor-v3";
import "md-editor-v3/lib/preview.css";
import CommonFooter from "@/components/CommonFooter.vue";
import CommonNavbar from "@/components/CommonNavbar.vue";
import guideSource from "@/content/newcomer-guide.md?raw";
</script>

<style scoped>
:deep(.md-editor-preview) { padding: 0; }
</style>
