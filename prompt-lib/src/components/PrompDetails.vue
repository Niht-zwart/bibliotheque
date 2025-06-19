<template>
    <div class="bg-gradient-to-b from-blue-900 to-blue-700 text-white py-10 px-6 min-h-[calc(100vh-80px)]">
        <!-- Titre + Description -->
        <div class="max-w-7xl mx-auto mb-8">
            <h1 class="text-4xl font-bold">{{ promptData.title }}</h1>
            <p class="text-blue-200 mt-2 text-lg">{{ promptData.description }}</p>
        </div>

        <div class="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-6">
            <!-- Colonne gauche -->
            <div class="space-y-6 col-span-1">
                <!-- Paramètres -->
                <div class="bg-white/10 p-4 rounded-xl">
                    <h2 class="text-xl font-semibold mb-4">Paramètres</h2>
                    <div v-for="param in parameterList" :key="param" class="mb-3">
                        <label :for="param" class="block text-sm font-medium text-blue-200">{{ param }}</label>
                        <input :id="param" v-model="parametres[param]" @input="updateText" :readonly="isEditing"
                            class="mt-1 block w-full rounded-md border border-blue-300 bg-white/80 p-2 text-blue-900 shadow-sm focus:ring focus:ring-blue-500 disabled:opacity-50" />
                    </div>
                </div>

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

            <!-- Colonne droite -->
            <div :class="isEditing ? 'col-span-1 md:col-span-2' : 'col-span-1 md:col-span-2'">
                <div class="flex justify-end mb-2 space-x-2 items-center">
                    <div class="text-sm text-blue-100 mr-auto italic">
                        Version du {{ promptData.date }}
                    </div>

                    <button v-if="!isEditing" @click="copyToClipboard"
                        class="bg-blue-100 text-blue-800 px-3 py-1 rounded hover:bg-blue-200 transition">
                        📋 Copier
                    </button>

                    <button @click="toggleEdit"
                        class="bg-yellow-400 text-blue-900 px-3 py-1 rounded hover:bg-yellow-500 transition">
                        {{ isEditing ? 'Annuler' : 'Modifier' }}
                    </button>

                    <button v-if="isEditing" @click="saveVersion"
                        class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600 transition">
                        💾 Enregistrer
                    </button>
                </div>

                <div v-if="isEditing">
                    <div
                        class="bg-white text-blue-900 p-6 rounded-xl shadow-md leading-relaxed h-[60vh] max-h-[60vh] overflow-y-auto whitespace-pre-wrap">
                        <PromptEditor v-model="promptData.text" @update:parameters="updateParametres($event)"
                            @update:promptText="promptText = $event" />
                    </div>
                </div>

                <div v-else
                    class="bg-white text-blue-900 p-6 rounded-xl shadow-md leading-relaxed h-[60vh] max-h-[60vh] overflow-y-auto whitespace-pre-wrap"
                    v-html="highlightedText"></div>
            </div>
        </div>

        <div v-if="showNotification"
            class="fixed bottom-5 right-5 bg-gray-800 text-white px-4 py-2 rounded shadow-lg transition-opacity duration-300">
            {{ notification }}
        </div>
    </div>
</template>


<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
import { createPrompt } from '@/stores/bibliotheque/api' // adapte le chemin si nécessaire
import { useBibliothequeStore } from '@/stores/bibliotheque/store'
import PromptEditor from '@/components/PromptEditor.vue'

const route = useRoute()
const categorie = route.params.categorie
const promptId = route.params.id

const store = useBibliothequeStore()
const { selectedElement } = storeToRefs(store)

const promptData = ref({
    id: '',
    title: '',
    text: '',
    parametres: [],
    history: []
})

const currentVersion = ref('latest')
const parameterList = ref([])
const parametres = ref({})
const notification = ref('')
const showNotification = ref(false)
const isEditing = ref(false)
const promptText = ref('')
const origine = ref({
    history: [],
    date: ''
})

onMounted(async () => {
    await init()
})

const init = async () => {
    currentVersion.value = 'latest'
    await store.loadElementById(promptId)
    const el = selectedElement.value

    promptData.value = {
        id: el.id,
        title: el.nom,
        description: el.description,
        text: el.contenu,
        parametres: el.parametres,
        date: el.date.toISOString().slice(0, 10),
        tags: el.tags,
        history: el.historiques?.map(h => ({
            date: h.date,
            text: h.contenu,
            parametres: h.parametres,
        })) ?? []
    }

    origine.value = { ...promptData.value }

    parameterList.value = promptData.value.parametres
    parametres.value = Object.fromEntries(parameterList.value.map((p) => [p, '']))
}

const highlightedText = computed(() => {
    let result = promptData.value.text
    parameterList.value.forEach((param) => {
        const raw = `$$${param}$$`
        const value = parametres.value[param] || raw
        const highlighted = `<mark class="bg-yellow-200 text-blue-900 px-1 rounded">${value}</mark>`
        result = result.replaceAll(raw, highlighted)
    })
    return result.replace(/\n/g, '<br/>')
})

const copyToClipboard = () => {
    let textToCopy = promptData.value.text

    // 1. Crée un élément temporaire
    const tempDiv = document.createElement('div')
    tempDiv.innerHTML = textToCopy

    // 2. Remplace les <br> par des \n
    tempDiv.querySelectorAll('br').forEach(br => br.replaceWith('\n'))

    // 3. Récupère le texte sans HTML
    textToCopy = tempDiv.textContent || tempDiv.innerText || ''

    parameterList.value.forEach((param) => {
        textToCopy = textToCopy.replaceAll(`$$${param}$$`, parametres.value[param] || '')
    })

    navigator.clipboard
        .writeText(textToCopy)
        .then(() => {
            notification.value = 'Prompt copié !'
            showNotification.value = true
            setTimeout(() => (showNotification.value = false), 3000)
        })
        .catch(() => {
            notification.value = 'Erreur lors de la copie.'
            showNotification.value = true
            setTimeout(() => (showNotification.value = false), 3000)
        })
}

const updateText = () => {
    // rien ici : juste placeholder pour future logique
}

const saveVersion = async () => {
    try {
        await createPrompt({
            id: promptData.value.id,
            nom: promptData.value.title,
            description: promptData.value.description,
            tags: promptData.value.tags,
            date: promptData.value.date,
            categorie: categorie,
            contenu: promptText.value,
            parametres: parameterList.value,
            history: promptData.value.history
        })
        notification.value = 'Prompt enregistré avec succès.'
        await init()
    } catch (e) {
        notification.value = 'Erreur lors de l’enregistrement.'
    }

    showNotification.value = true
    setTimeout(() => (showNotification.value = false), 3000)
    isEditing.value = false
}


const loadVersion = (index) => {
    currentVersion.value = index
    const version = promptData.value.history[index]
    promptData.value.text = version.text
    promptData.value.parametres = version.parametres
    promptData.value.date = version.date
    parameterList.value = promptData.value.parametres
    parametres.value = Object.fromEntries(parameterList.value.map((p) => [p, '']))
}

const loadOrigine = () => {
    currentVersion.value = 'latest'
    promptData.value = { ...origine.value };
    parameterList.value = promptData.value.parametres
    parametres.value = Object.fromEntries(parameterList.value.map((p) => [p, '']))
}

const toggleEdit = () => {
    if (isEditing.value) {
        promptData.value = { ...origine.value }
        parameterList.value = promptData.value.parametres
        parametres.value = Object.fromEntries(parameterList.value.map((p) => [p, '']))
    }
    isEditing.value = !isEditing.value
}

const updateParametres = (event) => {
    parameterList.value = event
    parametres.value = Object.fromEntries(parameterList.value.map((p) => [p, '']))
}
</script>