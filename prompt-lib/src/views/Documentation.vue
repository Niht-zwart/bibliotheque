<template>
    <div class="min-h-[calc(100vh-80px)] bg-gradient-to-b from-blue-900 to-blue-700 p-6 text-white">
        <div class="max-w-4xl mx-auto">

            <!-- Catégories avec ajout -->
            <div class="flex flex-wrap justify-center gap-4 mb-10 items-center">
                <!-- Boutons de catégorie -->
                <button v-for="category in documentCategories" class="px-5 py-2 rounded-xl font-semibold shadow" :class="selectedCategory === category
                    ? 'bg-blue-600 hover:bg-blue-500 text-white'
                    : 'bg-white text-blue-700'" @click="changeCategorie(category)">
                    {{ category }}
                </button>

                <!-- Bouton ou champ d'ajout -->
                <div class="relative">
                    <form v-if="addingCategory" @submit.prevent="addCategory" class="flex items-center space-x-2">
                        <input v-model="newCategory" placeholder="Nouvelle catégorie" autofocus
                            class="px-3 py-2 rounded-lg border border-blue-300 bg-blue-100 text-blue-900 placeholder:text-blue-600 w-48"
                            @keydown.esc="cancelCategory" />
                        <button type="submit" class="text-white font-bold text-lg">✔</button>
                        <button type="button" @click="cancelCategory" class="text-red-300 font-bold text-lg">✖</button>
                    </form>

                    <button v-if="!addingCategory" @click="addingCategory = true"
                        class="px-3 py-2 rounded-xl bg-white text-blue-700 font-bold shadow hover:bg-blue-100 transition">
                        ➕
                    </button>
                </div>
            </div>

            <!-- Message d'erreur -->
            <div v-if="categoryError" class="text-red-300 text-center font-semibold mb-4">
                {{ categoryError }}
            </div>

            <!-- Liens -->
            <div v-if="documents.length" class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-12">
                <a v-for="link in documents" :href="link.url" target="_blank"
                    class="bg-white/90 text-blue-900 p-4 rounded-xl shadow hover:shadow-lg transition block">
                    <h2 class="text-lg font-semibold mb-1">{{ link.titre }}</h2>
                    <p class="text-sm text-blue-600">{{ link.description }}</p>
                </a>
            </div>
            <div v-else class="text-center text-blue-200 italic mb-12">Aucun lien pour cette catégorie.</div>

            <!-- Bouton pour afficher le formulaire d'ajout de lien -->
            <div class="text-center mb-6">
                <button @click="showLinkForm = !showLinkForm"
                    class="px-6 py-3 bg-white text-blue-700 font-bold rounded-xl shadow hover:bg-blue-100 transition">
                    {{ showLinkForm ? '❌ Fermer le formulaire' : '➕ Ajouter un lien' }}
                </button>
            </div>

            <!-- Formulaire d'ajout de lien -->
            <transition name="fade">
                <div v-if="showLinkForm" class="bg-white/90 text-blue-900 p-6 rounded-xl shadow mb-12">
                    <h2 class="text-xl font-bold mb-4">Ajouter un lien</h2>
                    <form @submit.prevent="addLink">
                        <div class="mb-4">
                            <label class="block font-semibold mb-1">Titre</label>
                            <input v-model="nouveauDocument.titre" required class="w-full px-4 py-2 rounded border" />
                        </div>
                        <div class="mb-4">
                            <label class="block font-semibold mb-1">Description</label>
                            <input v-model="nouveauDocument.description" required
                                class="w-full px-4 py-2 rounded border" />
                        </div>
                        <div class="mb-4">
                            <label class="block font-semibold mb-1">URL</label>
                            <input v-model="nouveauDocument.url" type="url" required
                                class="w-full px-4 py-2 rounded border" />
                        </div>
                        <div class="mb-4">
                            <label class="block font-semibold mb-1">Catégorie</label>
                            <select v-model="nouveauDocument.categorie" required
                                class="w-full px-4 py-2 rounded border">
                                <option v-for="category in documentCategories" :key="category" :value="category">
                                    {{ category }}
                                </option>
                            </select>
                        </div>
                        <button type="submit" @click="addDocument"
                            class="bg-blue-700 text-white font-bold px-4 py-2 rounded hover:bg-blue-600 transition">
                            ➕ Ajouter
                        </button>
                    </form>
                </div>
            </transition>
        </div>
    </div>

    <div v-if="showNotification"
        class="fixed bottom-5 right-5 bg-gray-800 text-white px-4 py-2 rounded shadow-lg transition-opacity duration-300">
        {{ notification }}
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useBibliothequeStore } from '@/stores/bibliotheque/store'

const store = useBibliothequeStore()
const { documentCategories, documents } = storeToRefs(store)

const selectedCategory = ref('general')
const showLinkForm = ref(false)
const newCategory = ref('')
const addingCategory = ref(false)
const categoryError = ref('')
const nouveauDocument = ref({
    titre: '',
    description: '',
    url: '',
    categorie: 'general',
})
const notification = ref('')
const showNotification = ref(false)

onMounted(async () => {
    await store.fetchDocumentCategories();
    if (documentCategories) {
        selectedCategory.value = documentCategories.value[0]
        await store.fetchDocuments(selectedCategory.value)
        nouveauDocument.value.categorie = selectedCategory.value
    }
})


async function addDocument() {
    await store.saveDocument(nouveauDocument.value, selectedCategory.value)
    showNotif('Document enregistré !')
    nouveauDocument.value = {
        titre: '',
        description: '',
        url: '',
        categorie: selectedCategory.value,
    }
    showLinkForm.value = false
}

async function addCategory() {
    const label = newCategory.value.trim()

    if (!label) {
        categoryError.value = 'Le nom de la catégorie ne peut pas être vide.'
        return
    }

    if (documentCategories.value.some((cat) => cat === label)) {
        categoryError.value = 'Cette catégorie existe déjà.'
        return
    }

    await store.saveCategorie(label)
    showNotif('Catégorie enregistré !')
    documentCategories.value.push(label)
    selectedCategory.value = label
    nouveauDocument.value.category = label
    newCategory.value = ''
    addingCategory.value = false
    categoryError.value = ''
}

function cancelCategory() {
    newCategory.value = ''
    addingCategory.value = false
    categoryError.value = ''
}

async function changeCategorie(categorie) {
    selectedCategory.value = categorie
    nouveauDocument.value.categorie = categorie
    await store.fetchDocuments(categorie)
}

function showNotif(message) {
    notification.value = message
    showNotification.value = true
    setTimeout(() => (showNotification.value = false), 3000)
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
    transform: scale(0.95);
}
</style>
