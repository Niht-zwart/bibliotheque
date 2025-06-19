<template>
    <div class="min-h-[calc(100vh-80px)] bg-gradient-to-b from-blue-900 to-blue-700 text-white p-6">
        <div class="max-w-4xl mx-auto">
            <h1 class="text-3xl font-bold mb-6">
                {{ type === 'prompt' ? 'Prompts' : 'Instructions' }} –
                <span class="text-yellow-300">{{ tagName }}</span>
            </h1>

            <div v-if="items.length">
                <div v-for="item in items" :key="item.id" @click="goToDetail(item.id)"
                    class="bg-white text-blue-900 p-4 rounded-xl shadow mb-4 cursor-pointer hover:bg-blue-100 transition">
                    <h2 class="text-xl font-semibold">{{ item.nom }}</h2>
                    <p class="text-sm mt-2 line-clamp-3 whitespace-pre-wrap">{{ item.description }}</p>
                </div>
            </div>
            <div v-else class="text-center mt-10 text-gray-200">
                Aucun {{ type }} trouvé avec ce tag.
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchElementsByTag } from '@/stores/bibliotheque/api'

const route = useRoute()
const router = useRouter()

const type = route.params.type
const tagName = route.params.tagName

const items = ref([])

onMounted(async () => {
    try {
        items.value = await fetchElementsByTag(type, tagName)
    } catch (err) {
        console.error('Erreur lors de la récupération des éléments :', err)
    }
})

const goToDetail = (id) => {
    router.push(`/${type}/${id}`)
}
</script>
