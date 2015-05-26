define([
    'backbone',
    'tmpl/menu/main',
    'models/user'
], function(
    Backbone,
    tmpl,
    userModel
){

    var View = Backbone.View.extend({

        template: tmpl,
        user:userModel,

        events: {
            'click .js-logout': function() {
                this.user.destroy();
            }
        },

        initialize: function ($body) {
            $body.append(this.el);
            this.$el.hide();
            this.render();
            this.listenTo(this.user, this.user.identifyCompleteEvent + ' ' + this.user.loginCompleteEvent + ' ' + this.user.signupCompleteEvent + ' ' + this.user.logoutCompleteEvent, function() {
                this.render();
            });
        },
        
        render: function () {
            this.$el.html(this.template({name: this.user.get('name') + this.user.get('email') + this.user.get('password') + this.user.get('chips') }));
            return this;
        },
        
        show: function () {
            this.$el.show();
            this.trigger("show", this);
        },
        
        hide: function () {
            this.$el.hide();
        }

    });

    return View;
});