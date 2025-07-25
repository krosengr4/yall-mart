let productService;

class ProductService {

    photos = [];

    filter = {
        cat: undefined,
        minPrice: undefined, 
        maxPrice: undefined,
        queryString: () => {
            let qs = "";
            if(this.filter.cat){
                qs = `cat=${this.filter.cat}`;
            } 
            if(this.filter.minPrice) {
                const minP = `minPrice=${this.filter.minPrice}`;
                if(qs.length>0) {
                    qs+= `&${minP}`;
                } 
                else {
                    qs = minP;
                }
            } 
            if (this.filter.maxPrice){
                const maxP = `maxPrice=${this.filter.maxPrice}`;
                if(qs.length>0) {
                    qs += `&${maxP}`;
                } 
                else {
                    qs = maxP;
                }
            }
            return qs.length > 0 
            ? `?${qs}` 
            : "";
        }
    }

    constructor() {
        //load list of photos into memory
        axios.get("/images/products/photos.json")
            .then(response => {
                this.photos = response.data;
            });
        
    }

    hasPhoto(photo){
        return this.photos.filter(p => p == photo).length > 0;
    }

    addCategoryFilter(cat) {
        if(cat == 0) {
            this.clearCategoryFilter();
        } else {
            this.filter.cat = cat;
        }
    }

    addMinPriceFilter(price) {
        if(price == 0 || price =="") {
            this.clearMinPricefilter();
        } else{
            this.filter.minPrice = price;
        }
    }

    addMaxPriceFilter(price){
        if(price == 0 || price == "") {
            this.clearMaxPriceFilter();
        } else {
            this.filter.maxPrice = price;
        }
    }

    clearCategoryFilter() {
        this.filter.cat = undefined;
    }

    clearMinPricefilter() {
        this.filter.minPrice = undefined;
    }

    clearMaxPriceFilter() {
        this.filter.maxPrice = undefined;
    }

    search() {
        const url = `${config.baseUrl}/products${this.filter.queryString()}`;

        axios.get(url)
        .then(response => {
            let data = {};
            data.products = response.data;

            data.products.forEach(product => {
                if(!this.hasPhoto(product.imageUrl)) {
                    product.imageUrl = "no-image.jpg";
                }
            })

            templateBuilder.build('product', data, 'content', this.enableButtons);

        })

        .catch(error => {
            const data = {
                error: "Searching products failed."
            };

            templateBuilder.append("error", data, "errors")
        });
    }

    enableButtons() {
        const buttons = [...document.querySelectorAll(".add-button")];

        if(userService.isLoggedIn()) {
            buttons.forEach(button => {
                button.classList.remove("invisible")
            });
        } else {
            buttons.forEach(button => {
                button.classList.add("invisible")
            });
        }
    }
}