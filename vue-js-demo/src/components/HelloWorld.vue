<script setup>
import {onMounted, ref} from 'vue'

defineProps({
  msg: String
})

const count = ref(0)
const appMessage = ref("")

function click() {
  count.value++;
  window.JSBridge.postMessage(`web button click value:${count.value}`)
}

onMounted(() => {
  window.appMessage = function (msg){
    appMessage.value = msg;
  }

})
</script>

<template>

  <div class="card">
    <button type="button" @click="click">send message to app</button>
    <p>
      from app message: {{ appMessage }}
    </p>
  </div>

</template>

<style scoped>
.read-the-docs {
  color: #888;
}
</style>
