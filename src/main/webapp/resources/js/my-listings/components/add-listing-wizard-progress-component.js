let AddListingWizardProgressComponent = {
    init: function () {
        Vue.use(window.vuelidate.default);

        Vue.component('add-listing-wizard-progress-component', {
            template: '#add-listing-wizard-progress-component',
            props: {
                activeStep: Object
            },
            data: function () {
                return {
                    progress: '25%',
                    steps: [{
                        active: true,
                        activated: false
                    }, {
                        active: false,
                        activated: false
                    }, {
                        active: false,
                        activated: false
                    }, {
                        active: false,
                        activated: false
                    }]
                };
            },
            validations: function () {
                return {};
            },
            methods: {},
            created: function () {
                let activeStep = parseInt(this.activeStep);
                let calculatedProgress = (activeStep + 1) / this.steps.length;
                this.progress = calculatedProgress * 100 + '%';
                if (activeStep > 0) {
                    for (let i = 0; i < this.steps.length; i++) {
                        if (i < activeStep) {
                            this.steps[i].active = false;
                            this.steps[i].activated = true;
                        } else if (i === activeStep) {
                            this.steps[i].active = true;
                            this.steps[i].activated = false;
                        } else {
                            this.steps[i].active = false;
                            this.steps[i].activated = false;
                        }
                    }
                }
            }
        });
    }
};
