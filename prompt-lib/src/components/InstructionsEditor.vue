<template>
  <EditorContent :editor="editor" class="flex-grow overflow-auto border rounded border-gray-300 p-4" />
</template>

<script setup>
import { ref, onBeforeUnmount, onMounted, watch } from 'vue'
import { Editor, Extension } from '@tiptap/core'
import { Plugin, PluginKey } from 'prosemirror-state'
import { Decoration, DecorationSet } from 'prosemirror-view'
import { EditorContent } from '@tiptap/vue-3'
import Document from '@tiptap/extension-document'
import Paragraph from '@tiptap/extension-paragraph'
import Text from '@tiptap/extension-text'

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['update:promptText', 'update:parameters'])
const parameters = ref([])

const HighlightMarkdown = Extension.create({
  name: 'highlightMarkdown',
  addProseMirrorPlugins() {
    return [
      new Plugin({
        key: new PluginKey('highlightMarkdown'),
        state: {
          init() {
            return DecorationSet.empty
          },
          apply(tr, old) {
            if (!tr.docChanged && !tr.getMeta('force-decorations')) return old

            const decorations = []
            const regexes = [
              // Headers: # Title, ## Title, etc.
              { regex: /(^|\n)(#{1,6})\s+([^\n]+)/g, className: 'md-header' },

              // Bold: **text** or __text__, but not inside other marks
              { regex: /(?<!\*)\*\*(?!\s)(.+?)(?<!\s)\*\*(?!\*)/g, className: 'md-bold' },
              { regex: /(?<!_)__(?!\s)(.+?)(?<!\s)__(?!_)/g, className: 'md-bold' },

              // Italic: *text* or _text_, but not **bold**
              { regex: /(?<!\*)\*(?!\*)(?!\s)(.+?)(?<!\s)\*(?!\*)/g, className: 'md-italic' },
              { regex: /(?<!_)_(?!_)(?!\s)(.+?)(?<!\s)_(?!_)/g, className: 'md-italic' },

              // Blockquote: > text
              { regex: /(^|\n)&gt;\s?(.*)/g, className: 'md-blockquote' }, // TipTap encodes > to &gt;

              // Inline code: `code`
              { regex: /`([^`\n]+?)`/g, className: 'md-inline-code' },
            ]

            tr.doc.descendants((node, pos) => {
              if (!node.isText) return
              const text = node.text

              regexes.forEach(({ regex, className }) => {
                let match
                while ((match = regex.exec(text)) !== null) {
                  let start = pos + match.index
                  let end = start + match[0].length

                  // For header, highlight only the header text (exclude newline)
                  if (className === 'md-header') {
                    start = pos + match.index + match[1].length + match[2].length + 1
                    end = start + match[3].length
                  }



                  decorations.push(Decoration.inline(start, end, {
                    class: className,
                  }))
                }
              })
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
    Document,
    Paragraph,
    Text,
    HighlightMarkdown,
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

<style>
.md-header {
  color: #d73a49;
  font-weight: bold;
}

.md-bold {
  font-weight: 700;
  color: #24292e;
  background-color: #fffbdd;
  border-radius: 3px;
  padding: 0 2px;
}

.md-italic {
  font-style: italic;
  color: #24292e;
  background-color: #f6f8fa;
  border-radius: 3px;
  padding: 0 2px;
}

.md-blockquote {
  color: #6a737d;
  font-style: italic;
  border-left: 4px solid #dfe2e5;
  margin-left: 0;
  padding-left: 10px;
}

.md-inline-code {
  font-family: monospace;
  background-color: #f6f8fa;
  border-radius: 3px;
  padding: 0 4px;
  color: #e36209;
}
</style>
