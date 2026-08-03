<template>
  <section class="admin-page">
    <header class="admin-page__header">
      <h1>{{ title }}</h1>
      <div v-if="$slots.action" class="admin-page__action"><slot name="action" /></div>
    </header>
    <div class="admin-page__content"><slot /></div>
  </section>
</template>

<script setup>
defineProps({ title: { type: String, required: true } });
</script>

<style scoped>
.admin-page { width: min(100%, 1200px); margin: 0 auto; }
.admin-page__header { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 16px; }
.admin-page__header h1 { margin: 0; color: #1d2939; font-size: 20px; line-height: 32px; font-weight: 650; }
.admin-page__action { display: flex; flex: none; gap: 8px; }
.admin-page__content { padding: 16px; background: #fff; border: 1px solid #eaecf0; border-radius: 10px; box-shadow: 0 1px 2px rgb(16 24 40 / 3%); }
@media (max-width: 768px) { .admin-page__header { margin-bottom: 12px; } .admin-page__header h1 { font-size: 18px; } .admin-page__content { padding: 12px; border-radius: 8px; } }
</style>
