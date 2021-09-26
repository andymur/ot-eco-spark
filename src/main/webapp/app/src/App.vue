<template>  
  <div id="app">
    <link rel="stylesheet" href="https://unpkg.com/vue-select@latest/dist/vue-select.css"/>
    <div id="nav">      
    </div>
    <router-view />
  </div>
</template>

<style>

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#nav {
  padding: 30px;
}

.selector {
  font-size: 1.5em;
  margin: 1em 0em;
  width: 60%;
}

.date-selector {
  text-align: left;
}

.date-selector select {
  font-size: 1.5em;  
}

.date-selector input {
  font-size: 1.5em;
  margin-left: 1em;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#search {
  font-size: 1.5em;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
