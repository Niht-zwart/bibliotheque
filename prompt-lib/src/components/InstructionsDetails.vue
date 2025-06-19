<template>
    <div class="min-h-[calc(100vh-80px)] bg-gradient-to-b from-blue-900 to-blue-700 text-white py-10 px-6">

        <div class="max-w-7xl mx-auto mb-8">
            <h1 class="text-4xl font-bold">{{ promptData.title }}</h1>
            <p class="text-blue-200 mt-2 text-lg">{{ promptData.description }}</p>
        </div>

        <div class="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-6">

            <!-- Colonne gauche -->
            <div class="space-y-6 col-span-1">
                <!-- Historique -->
                <div class="bg-white/10 p-4 rounded-xl">
                    <h2 class="text-xl font-semibold mb-4">Historique des versions</h2>
                    <div class="space-y-2 max-h-[50vh] overflow-y-auto pr-1">
                        <button v-for="(version, index) in promptData.history" :key="index"
                            @click="!isEditing && loadVersion(index)" :disabled="isEditing" :class="[
                                'block w-full text-left rounded-md px-4 py-2 transition text-base',
                                isEditing ? 'bg-white/50 text-blue-400 cursor-not-allowed' :
                                    (currentVersion == index
                                        ? 'bg-yellow-300 text-blue-900 font-semibold text-lg'
                                        : 'bg-white/80 text-blue-900 hover:bg-white')
                            ]">
                            📄 Version {{ index + 1 }} – {{ version.date }}
                        </button>

                        <button v-if="date !== ''" @click="!isEditing && loadOrigine()" :disabled="isEditing" :class="[
                            'block w-full text-left rounded-md px-4 py-2 transition text-base',
                            isEditing ? 'bg-white/50 text-blue-400 cursor-not-allowed' :
                                (currentVersion === 'latest'
                                    ? 'bg-yellow-300 text-blue-900 font-semibold text-lg'
                                    : 'bg-white/80 text-blue-900 hover:bg-white')
                        ]">
                            📄 Version {{ origine.history.length + 1 }} – {{ origine.date }}
                        </button>
                    </div>
                </div>
            </div>

            <div :class="isEditing ? 'col-span-1 md:col-span-2' : 'col-span-1 md:col-span-2'">

                <div class="flex justify-end mb-2 space-x-2 items-center">
                    <div class="text-sm text-blue-100 mr-auto italic">
                        Version du {{ promptData.date }}
                    </div>

                    <button @click="downloadMarkdown"
                        class="bg-blue-100 text-blue-800 px-3 py-1 rounded hover:bg-blue-200 transition">
                        📥 Télécharger .md
                    </button>

                    <router-link :to="`/${route.params.categorie}/${route.params.id}/edit/${currentVersion}`">
                        <button class="bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded">
                            ✏️ Modifier
                        </button>
                    </router-link>
                </div>

                <pre
                    class="h-[60vh]"><code ref="highlightedContent" class="language-markdown markdown-preview hljs h-[60vh]"></code></pre>

            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useBibliothequeStore } from '@/stores/bibliotheque/store'
import hljs from 'highlight.js/lib/core'
import markdown from 'highlight.js/lib/languages/markdown'
import 'highlight.js/styles/github-dark.css'

hljs.registerLanguage('markdown', markdown)

const route = useRoute()
const instructionId = route.params.id

const store = useBibliothequeStore()
const { selectedElement, selectedCategorie } = storeToRefs(store)

const promptData = ref({
    id: '',
    title: '',
    text: '',
    parametres: [],
    history: []
})

const isEditing = ref(false)
const origine = ref({
    history: [],
    date: ''
})
const historyList = ref([])
const highlightedContent = ref(null)
const currentVersion = ref('latest')

onMounted(async () => {
    await store.loadElementById(instructionId)
    const el = selectedElement.value

    promptData.value = {
        id: el.id,
        title: el.nom,
        description: el.description,
        text: el.contenu,
        date: el.date.toISOString().slice(0, 10),
        tags: el.tags,
        history: el.historiques?.map(h => ({
            date: h.date,
            text: h.contenu,
            parametres: h.parametres,
        })) ?? []
    }

    origine.value = { ...promptData.value }

    nextTick(renderHighlight)
})

const renderHighlight = () => {
    if (highlightedContent.value) {
        const element = highlightedContent.value

        // Reset previous highlight state if any
        if (element.dataset.highlighted) {
            delete element.dataset.highlighted
        }

        element.textContent = sanitizeHtmlToMarkdownishText(promptData.value.text)
        hljs.highlightElement(element)
    }
}
const loadVersion = (idx) => {
    promptData.value.text = promptData.value.history[idx].text
    currentVersion.value = idx
    nextTick(renderHighlight)
}

const loadOrigine = () => {
    promptData.value = { ...origine.value };
    currentVersion.value = 'latest'
    nextTick(renderHighlight)
}

const downloadMarkdown = () => {
    const blob = new Blob([sanitizeHtmlToMarkdownishText(promptData.value.text)], { type: 'text/markdown' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `${promptData.value.title || 'contenu'}.md`
    a.click()
    URL.revokeObjectURL(url)
}

function sanitizeHtmlToMarkdownishText(html) {
    return html
        .replace(/<p>\s*(?:&nbsp;|\s)*<\/p>/gi, '\n')          // Supprime les <p> vides
        .replace(/<br\s*\/?>/gi, '\n')                        // <br> → nouvelle ligne
        .replace(/<\/p>/gi, '\n')                             // </p> → nouvelle ligne
        .replace(/<p[^>]*>/gi, '')                            // supprime les balises <p>
        .replace(/<\/?[^>]+(>|$)/g, '')                       // supprime tout le HTML restant
        .trim()
}
</script>

<style scoped>
.markdown-preview {
    white-space: pre-wrap;
    padding: 1rem;
    background-color: black;
    border-radius: 0.5rem;
    font-family: monospace;
    font-size: 0.95rem;
}

.markdown-editor {
    min-height: 300px;
}
</style>
