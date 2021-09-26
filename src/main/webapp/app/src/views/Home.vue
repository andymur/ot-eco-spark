<!-- https://learnvue.co/2021/01/everything-you-need-to-know-about-vue-v-model/ -->
<!-- https://github.com/djaxho/pure-vue-chart -->
<template>
  <div class="home">
    <div class="selector">
      <v-select v-model="country" :options="countryOptions" id="country-select"></v-select>
    </div>
    <div class="selector">
      <v-select v-model="city" :style="{visibility: country ? 'visible' : 'hidden'}" :options="cityOptions" id="city-select"></v-select>
    </div>
    <div class="selector">
      <v-select v-model="tags" multiple :options="tagOptions" id="tags-select"></v-select>    
    </div>
    <input type="button" @click="search" value="Search" id="search">
    <p/>
    <div>
      <pure-vue-chart :points="points" :width="1200" :height="400" :show-y-axis="true" :show-x-axis="true" :show-values="true" />
    </div>
  </div>  
</template>

<script>
// @ is an alias to /src

import axios from "axios";

export default {
  name: "about",
  components: {

  },
  mounted: function() {
    console.log("App has been mounted!");
    var self = this;
    axios
      .get("http://localhost:5000/tags/")
      .then(response => (this.tagOptions = response.data.payload))
      .catch(function(error) {
        self.tags = [];
        console.error("cannot read tags list from server " + error);
      });


    axios
      .get("http://localhost:5000/countries/")
      .then(response => (this.countryOptions = response.data.payload))
      .catch(function(error) {
        self.countryOptions = [];
        console.error("cannot read countries list from server " + error);
      });
  },
  data: function() {
    return {      
      tags: [],
      tagOptions: [],
      country: "",
      city: "",
      countryOptions : [],
      cityOptions : [],
      points: [
                {label: 'Jan 20', value: 1}, {label: 'Feb 20', value: 2}, {label: 'Mar 20', value: 3}, {label: 'Apr 20', value: 4}, {label: 'Jun 21', value: 5},
                {label: 'Jan 20', value: 1}, {label: 'Feb 20', value: 2}, {label: 'Mar 20', value: 3}, {label: 'Apr 20', value: 4}, {label: 'Jun 21', value: 5},
                {label: 'Jan 20', value: 1}, {label: 'Feb 20', value: 2}, {label: 'Mar 20', value: 3}, {label: 'Apr 20', value: 4}, {label: 'Jun 21', value: 5},
                {label: 'Jan 20', value: 1}, {label: 'Feb 20', value: 2}, {label: 'Mar 20', value: 3}, {label: 'Apr 20', value: 4}, {label: 'Jun 21', value: 5},
                {label: 'Jan 20', value: 1}, {label: 'Feb 20', value: 2}, {label: 'Mar 20', value: 3}, {label: 'Apr 20', value: 4}, {label: 'Jun 21', value: 5},
                {label: 'Jan 20', value: 1}, {label: 'Feb 20', value: 2}, {label: 'Mar 20', value: 3}, {label: 'Apr 20', value: 4}, {label: 'Jun 21', value: 14}
              ]
    };
  },
  methods: {
    search: function () {
      axios
        .post("http://localhost:5000/jobstatistics/", 
          {'fromPeriod': '2020-01', 'toPeriod': '2021-01'/*, 'tags': this.tags, 'city': this.city, 'land': this.country*/},
          {
            headers: {
            'content-type': 'application/json'
            }
          }
        )
        .then(response => this.convertSearchResult(response.data.payload))
        .catch(function(error) {
          self.points = [];
          console.error("cannot obtain search result from server " + error);
        });
    },
    convertSearchResult: function(searchResult) {
      let newPoints = []
      searchResult.forEach(element => {
        newPoints.push({label: this.convertMonth(element.month) + " " + element.year, value: element.count})
      });
      this.points = newPoints;
    },
    convertMonth: function (monthNumber) {
      let monthDict = {1: "Jan", 2: "Feb", 3: "Mar", 4: "Apr", 5: "May", 6: "Jun", 7: "Jul", 8: "Aug", 9: "Sep", 10: "Oct", 11: "Nov", 12: "Dec"};
      return monthDict[monthNumber];
    }
  },
  watch: {
    country: function (newVal) {
      if (newVal) {
        axios
        .get("http://localhost:5000/cities/" + newVal)
        .then(response => (this.cityOptions = response.data.payload))
        .catch(function(error) {
          self.countryOptions = [];
          console.error("cannot read countries list from API " + error);
        });
      }
    }
  }
};
</script>
