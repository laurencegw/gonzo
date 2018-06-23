<template>
    <div id="app">
        <div class="title">
            GONZO CODE
        </div>
        <div class="subtitle">
            Learning by building stuff that has probably been built before, and better.
        </div>
        <div id="nav">
            <router-link to="/tags">Tags</router-link>
            |
            <router-link to="/frontpage">Front Page</router-link>
            |
            <router-link to="/authors">Authors</router-link>
            |
            <router-link to="/me">Me</router-link>
        </div>
        <router-view/>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator"

    @Component
    export default class App extends Vue {
    }
</script>

<style lang="scss">
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
    }

    #nav {
        padding: 30px;
        a {
            font-weight: bold;
            color: #2c3e50;
            &.router-link-exact-active {
                color: #42b983;
            }
        }
    }
</style>
