<template>
    <header class="bg-blue-900 text-white py-4 relative min-h-[80px]">
        <div class="max-w-6xl mx-auto px-6 text-center">
            <router-link to="/" class="inline-flex items-center space-x-2 text-2xl font-bold hover:underline">
                <h1 class="text-4xl font-bold">📚 Bibliothèque de Prompts</h1>
            </router-link>
        </div>
        <router-link to="/documentation"
            class="absolute right-6 top-1/2 -translate-y-1/2 bg-white text-blue-900 font-semibold px-4 py-2 rounded hover:bg-gray-100 transition">
            Documentation
        </router-link>
    </header>
</template>
