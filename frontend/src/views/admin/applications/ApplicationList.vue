<template>
  <AdminPage title="报名管理">
    <ApplicationPicker removable exportable :initial-year="currentYear" />
  </AdminPage>
</template>

<script setup>
import AdminPage from "@/components/admin/AdminPage.vue";
import ApplicationPicker from "@/components/admin/ApplicationPicker.vue";

// 报名管理入口默认查询当前招新批次年份
const currentYear = new Date().getFullYear();
</script>
