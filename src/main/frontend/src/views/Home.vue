<script>
const triggerTabList = document.querySelectorAll('#myTab button')
triggerTabList.forEach(triggerEl => {
  const tabTrigger = new bootstrap.Tab(triggerEl)

  triggerEl.addEventListener('click', event => {
    event.preventDefault()
    tabTrigger.show()
  })
})

export default {
  data () {
    return {
      disabled: false,
    }
  },

  methods: {
    switchDisabledStatus () {
      this.disabled = !this.disabled;
    },
    openItems () {
      this.switchDisabledStatus();
    },
    // checkClick () {
    //   triggerTabList.forEach(triggerEl => {
    //     const tabTrigger = new bootstrap.Tab(triggerEl)

    //     triggerEl.addEventListener('click', event => {
    //       event.preventDefault()
    //       tabTrigger.show()
    //     })
    //   })
    // }
  },

  mounted () {
    // console.log(triggerEl);
  }
}
</script>

<template>
  <h2>Home</h2>

  <nav>
    <div class="nav nav-tabs" id="nav-tab" role="tablist">
      <button
        class="nav-link active"
        id="nav-compras-tab"
        data-bs-toggle="tab"
        data-bs-target="#nav-compras"
        type="button"
        role="tab"
        aria-controls="nav-compras"
        aria-selected="true"
        >Compras</button>
      <button
        class="nav-link"
        id="nav-itens-tab"
        data-bs-toggle="tab"
        data-bs-target="#nav-itens"
        type="button"
        role="tab"
        aria-controls="nav-itens"
        aria-selected="false"
        :disabled="this.disabled"
        >Itens</button>
    </div>
  </nav>
  <div class="tab-content" id="nav-tabContent">
    <div class="tab-pane fade show active" id="nav-compras" role="tabpanel" aria-labelledby="nav-compras-tab" tabindex="0">
      <button
        id="nav-itens-tab-inside-compras"
        @click="checkClick"
      >vai para Itens</button>
    </div>
    <div class="tab-pane fade" id="nav-itens" role="tabpanel" aria-labelledby="nav-itens-tab" tabindex="0">...</div>
  </div>

</template>

<style scope>
</style>