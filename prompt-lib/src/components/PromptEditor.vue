<template>
  <EditorContent :editor="editor" class="flex-grow overflow-auto border rounded border-gray-300 p-4" />
</template>

<script setup>
import { ref, onBeforeUnmount, onMounted, watch } from 'vue'
import { Editor, Extension } from '@tiptap/core'
import { Plugin, PluginKey } from 'prosemirror-state'
import { Decoration, DecorationSet } from 'prosemirror-view'
import StarterKit from '@tiptap/starter-kit'
import { EditorContent } from '@tiptap/vue-3'
import Placeholder from '@tiptap/extension-placeholder'

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['update:promptText', 'update:parameters'])
const parameters = ref([])

const HighlightParams = Extension.create({
  name: 'highlightParams',
  addProseMirrorPlugins() {
    return [
      new Plugin({
        key: new PluginKey('highlightParams'),
        state: {
          init() {
            return DecorationSet.empty
          },
          apply(tr, old) {
            if (!tr.docChanged && !tr.getMeta('force-decorations')) return old
            const decorations = []
            const regex = /\$\$(.+?)\$\$/g
            tr.doc.descendants((node, pos) => {
              if (!node.isText) return
              let text = node.text
              let match
              while ((match = regex.exec(text)) !== null) {
                const start = pos + match.index
                const end = start + match[0].length
                decorations.push(Decoration.inline(start, end, {
                  class: 'bg-yellow-200 text-blue-900 rounded px-1',
                }))
              }
            })
            return DecorationSet.create(tr.doc, decorations)
          }
        },
        props: {
          decorations(state) {
            return this.getState(state)
          }
        }
      })
    ]
  }
})

onMounted(() => {
  editor.commands.focus('end')

  // 🔥 Forcer un "docChanged" pour que le plugin s'active sur le contenu initial
  const { state, view } = editor
  const tr = state.tr.setMeta('force-decorations', true)
  view.dispatch(tr)
})

const editor = new Editor({
  extensions: [
    StarterKit,
    HighlightParams,
    Placeholder.configure({
      placeholder: 'Votre prompt ici ...',
      showOnlyWhenEditable: true,
    }),
  ],
  content: props.modelValue,
  editable: true,
  onUpdate({ editor }) {
    const text = editor.getText()
    extractParameters(text)
    emit('update:parameters', parameters.value)
    emit('update:promptText', editor.getHTML())
  },
})

function extractParameters(text) {
  const regex = /\$\$(.+?)\$\$/g
  const found = new Set()
  let match
  while ((match = regex.exec(text)) !== null) {
    found.add(match[1].trim())
  }
  parameters.value = Array.from(found)
}

onBeforeUnmount(() => {
  editor.destroy()
})
</script>
