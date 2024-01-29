// createWebHistory => view router production htaxis
import { createRouter, createWebHashHistory } from 'vue-router';
import Compra from '../views/Compra.vue';

const routes = [
  { path: '/login', component: () => import ('../views/Login.vue') },
  { path: '/', component: () => import('../views/Home.vue') },
  { path: '/obra', name: 'obra', component: () => import('../views/Obra.vue') },
  { path: '/compra', name: 'compra', component: Compra },
  { path: '/relatorio', component: () => import('../views/Relatorio.vue') },
  { path: '/dashboard', component: () => import('../views/Dashboard.vue') },
  { path: '/socio', component: () => import('../views/Socio.vue') },
  { path: '/localuso', component: () => import('../views/LocalUso.vue') },
  { path: '/fornecedor', component: () => import('../views/Fornecedores.vue') },
  { path: '/produto', component: () => import('../views/Produto.vue') },
  { path: '/marca', component: () => import('../views/Marca.vue') },
  { path: '/unidademedida', component: () => import('../views/UnidadeMedida.vue') },
  { path: '/arquivo', component: () => import('../views/Arquivos.vue') },
  { path: '/usuario', component: () => import('../views/Usuario.vue') },
  { path: '/perfil', component: () => import('../views/Perfil.vue') },
  { path: '/configuracoes', component: () => import('../views/Configuracoes.vue') },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
