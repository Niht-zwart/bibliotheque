import { defineStore } from 'pinia'
import { BibliothequeTag } from './model'
import { fetchBibliotheque, fetchElementsByTag, fetchElementById, fetchTagsByNomAndCategorie, fetchCategorie, fetchDocumentCategories, fetchDocumentsByCategorie, saveCategorieDocuments, saveDocument } from './api'

export const useBibliothequeStore = defineStore('bibliotheque', {
  state: () => ({
    elements: [],
    selectedElement: null,
    tags: [],
    categories: {},
    selectedCategorie: null,
    documentCategories: [],
    documents: []
  }),

  actions: {
    async loadBibliotheque() {
      const data = await fetchBibliotheque()

      this.categories = Object.entries(data).reduce((acc, [key, value]) => {
        acc[key] = value.map(item => new BibliothequeTag(item))
        return acc
      }, {})
    },
    async loadCategorie(nom) {
      this.selectedCategorie = await fetchCategorie(nom)
    },

    async loadElementsByTag(type, tag) {
      this.elements = await fetchElementsByTag(type, tag)
    },

    async loadElementById(id) {
      this.selectedElement = await fetchElementById(id)
    },
    async loadTagsByNomAndCategorie(nom, categorie) {
      this.tags = await fetchTagsByNomAndCategorie(nom, categorie)
    },
    async fetchDocumentCategories() {
      this.documentCategories = await fetchDocumentCategories()
    },
    async fetchDocuments(categorie) {
      this.documents = await fetchDocumentsByCategorie(categorie)
    },
    async saveCategorie(categorie) {
      await saveCategorieDocuments(categorie)
      await fetchDocumentCategories()
      await this.fetchDocuments(categorie)
    },
    async saveDocument(document, categorie) {
      await saveDocument(document)
      await this.fetchDocuments(categorie)
    }
  }
})
