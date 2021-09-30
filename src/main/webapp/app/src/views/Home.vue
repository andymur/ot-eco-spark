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
    <div class="date-selector">
      <!-- TODO: component me -->
      <select name="fromMonth" v-model="fromMonth" class="month-selector">
        <option value="null" selected>---</option>
        <option value="1">Jan</option>
        <option value="2">Feb</option>
        <option value="3">Mar</option>
        <option value="4">Apr</option>
        <option value="5">May</option>
        <option value="6">Jun</option>
        <option value="7">Jul</option>
        <option value="8">Aug</option>
        <option value="9">Sep</option>
        <option value="10">Oct</option>
        <option value="11">Nov</option>
        <option value="12">Dec</option>
      </select>
      <input type="text" value="2020" v-model="fromYear" class="year-selector" />
    </div>
    <div class="date-selector">
      <!-- TODO: component me -->
      <select name="fromMonth" v-model="toMonth" class="month-selector">
        <option value="null" selected>---</option>
        <option value="1">Jan</option>
        <option value="2">Feb</option>
        <option value="3">Mar</option>
        <option value="4">Apr</option>
        <option value="5">May</option>
        <option value="6">Jun</option>
        <option value="7">Jul</option>
        <option value="8">Aug</option>
        <option value="9">Sep</option>
        <option value="10">Oct</option>
        <option value="11">Nov</option>
        <option value="12">Dec</option>
      </select>
      <input type="text" value="2021" v-model="toYear" class="year-selector" />
    </div>
    <input type="button" @click="search" value="Search" id="search">
    <p/>
    <div>
      <pure-vue-chart :style="{visibility: points.length > 0 ? 'visible' : 'hidden'}" :points="points" :width="1200" :height="400" :show-y-axis="true" :show-x-axis="true" :show-values="true" />
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
    var self = this;
    axios
      .get("http://localhost:5000/tags/")
      .then(response => (this.tagOptions = response.data.payload))
      .catch(function(error) {
        self.tags = [];
      });


    axios
      .get("http://localhost:5000/countries/")
      .then(response => (this.countryOptions = response.data.payload))
      .catch(function(error) {
        self.countryOptions = [];
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
      points: [],
      fromMonth: null,
      fromYear: 2019,
      toMonth: null,
      toYear: 2021
    };
  },
  methods: {
    search: function () {
      this.points = []
      let request = {
                      "fromPeriod": this.convertRequestPeriod(this.fromMonth, this.fromYear), 
                      "toPeriod": this.convertRequestPeriod(this.toMonth, this.toYear),
                      "city": this.city,
                      "country": this.country,
                      "tags": this.tags
                    };
      console.log(request);
      axios
        .post("http://localhost:5000/jobstatistics/", 
          request,
          {
            headers: {
            'content-type': 'application/json'
            }
          }
        )
        .then(response => this.convertSearchResult(response.data.payload))
        .catch(function(error) {
          self.points = [];
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
    },
    convertRequestPeriod: function(monthValue, yearValue) {
      if (monthValue == null || monthValue == "" || yearValue == "") {
        return null;
      }
      if (monthValue < 10) {
        return yearValue + "0" + monthValue;
      } else {
        return yearValue +  monthValue;
      }
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
        });
      }
    }
  }
};
</script>
