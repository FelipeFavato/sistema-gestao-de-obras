<script>
import { toast} from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

export default {
  methods: {
    showToast () {
      toast.info('Wow info!', {
        autoClose: 1000
      })
    }
  }
}

</script>

<template>
  <button type="button" @click="$event => showToast()" class="btn btn-info">info</button>
</template>