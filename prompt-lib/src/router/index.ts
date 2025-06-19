import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import TagPrompts from '@/views/TagPrompts.vue'
import PrompDetails from '@/components/PrompDetails.vue'
import InstructionsDetails from '@/components/InstructionsDetails.vue'
import InstructionsEdit from '@/views/InstructionsEdit.vue'
import AddScriptToCategorie from '@/views/AddScriptToCategorie.vue'
import ScriptDetails from '@/views/ScriptDetails.vue'
import Documentation from '@/views/Documentation.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/documentation',
      name: 'Documentation',
      component: Documentation
    },
    { 
      path: '/:type/tag/:tagName', 
      name: 'TagPrompts', 
      component: TagPrompts 
    },
    {
      path: '/:categorie/:id',
      name: 'ScriptDetails',
      component: ScriptDetails
    },
    {
      path: '/:categorie/:id/edit/:version',
      name: 'InstructionEdit',
      component: InstructionsEdit
    },
    {
      path: '/create/:categorie',
      name: 'AddScriptToCategorie',
      component: AddScriptToCategorie
    }
  ],
})

export default router
