/*
 * Copyright 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
$.register_module({
    name: 'og.analytics.form',
    dependencies: ['og.common.util.ui.AutoCombo', 'og.views.common.layout'],
    obj: function () { 
        return function (selector) {
            var FormCombo, Status, create_form, handle_error, ds_response = { // dummy
                    type: ['Live', 'Snapsot', 'Historical', 'Data Type'],
                    live: ['Bloomberg', 'Reuters'],
                    snapshot: ['Alan', 'Alan 2'],
                    historical: ['01 June 2012', '02 June 2012', '03 June 2012'],
                    datatype: ['type 1', 'type 2'],
                    row: [
                        {num: 1, type: 'Live', value: 'Bloomberg'},
                        {num: 2, type: 'Snapshot', value: 'Alan'},
                        {num: 3, type: 'Historical', value: '02 June 2012'},
                        {num: 4, type: 'Data Type', value: 'type 2', last: true}
                    ]
            };
            FormCombo = function (selector, module, data) {
                var FormCombo = this, Title, Menu;
                Title = function (selector, markup){
                    var title = this, $(selector).html(markup).find('.og-option-title').on('click', function (event) {
                            event.stopPropagation();
                            FormCombo.menu.state === 'open' ? FormCombo.menu.close() : FormCombo.menu.open().focus();
                        }), return title;
                };
                Menu = function () {
                    var menu = this, popdiv = $(selector + ' .OG-analytics-form-menu'), menu.state = 'closed',
                        menu.focus = function () {
                            return popdiv.find('select').first().focus(), menu; 
                        },
                        menu.open = function () {
                            return popdiv.show().blurkill(menu.close).trigger('open', title), 
                                title.addClass('og-active'), menu.state = 'open', menu;
                        },
                        menu.close = function () { 
                            return (popdiv ? popdiv.hide() : null), title.removeClass('og-active'),
                                menu.state = 'closed', menu;
                        }, return menu;
                };
                $.when(og.api.text({module: module})).then(function (template) {
                    FormCombo.title = new Title(selector, $((Handlebars.compile(template))(data)));
                    FormCombo.menu = new Menu(selector);
                });
            };
            Status = function (selector) {
                var status = this, interval, init = false;
                $(selector + ' button').on('click', function () {
                    if (!status.status || status.status === 'paused') return status.play();
                    if (status.status === 'playing') return status.pause();
                });
                status.pause = function () {
                    $(selector + ' em').html('paused').removeClass('live').addClass('paused');
                    $(selector + ' button').removeClass('og-icon-play').addClass('og-icon-pause');
                    status.message('');
                    clearInterval(interval);
                    status.status = 'paused';
                };
                status.play = function () {
                    if (!init) init = !!$(selector + ' button').removeClass('og-disabled');
                    $(selector + ' em').html('live').removeClass('paused').addClass('live');
                    $(selector + ' button').removeClass('og-icon-pause').addClass('og-icon-play');
                    status.message('starting...');
                    interval = setInterval(function () {
                        status.message('updated ' + (Math.random() + 1).toFixed(2) + ' seconds ago');
                    }, 1000);
                    status.status = 'playing';
                };
                status.message = function (message) {$(selector + ' .og-message').html(message);};
                status.status = null;
                return status;
            };
            create_form = function (template, search, aggregators) {
                $(selector).html(template)
                    /**
                     * Custom tab event, only triggers between top level menu items. e.g. a tab event will trigger
                     * when tab entering or tab leaving the evented element
                     */
                    .on('keydown', 'input, select, button', function (event) {
                        if (event.keyCode !== 9) return;
                        var $aggr = $(selector + ' .og-aggregation').find('input, select, button'),
                            $data = $(selector + ' .og-datasources').find('input, select, button'),
                            $self = $(this), shift_key = event.shiftKey,
                            trigger = function (menu) {$self.trigger('tab', menu);},
                            active_pos = function (elms, pos) {return $self.is(elms[pos]())},
                            view = $self.closest('.og-view').length,
                            aggregation = $self.closest('.og-aggregation').length,
                            datasources = $self.closest('.og-datasources').length,
                            load = $self.hasClass('og-load');
                        if (view && shift_key) return;
                        if (view && !shift_key) return trigger('aggregation');
                        if (aggregators && active_pos($aggr, 'last') && !shift_key) return trigger('datasources');
                        if (datasources && active_pos($data, 'first') && shift_key) return trigger('aggregation');
                        if (datasources && active_pos($data, 'last') && !shift_key) return trigger('load');
                        if (load && shift_key) return trigger('datasources');
                        })
                    .on('tab', function (event, type) {
                        switch (type) {
                            case 'aggregation': aggregation_menu.open(); break;
                            case 'datasources': datasources_menu.open(); break;
                            case 'load': aggregation_menu.close(); datasources_menu.close(); break;
                        }
                        })
                    /**
                     * The "open" event fires everytime a menu item is opened
                     */
                    .on('open', function (event, elm) {
                        if (!aggregation_menu || !datasources_menu) return;
                        var elem = $(elm);
                        if (elem.closest('.og-view').length) datasources_menu.close(), aggregation_menu.close();
                        if (elem.closest('.og-aggregation').length) datasources_menu.close();
                        if (elem.closest('.og-datasources').length) aggregation_menu.close();
                    })
                    .on('click', '.og-menu-aggregation button', function () {
                        var val = $(this).text();
                        if (val === 'OK') $(selector).trigger('tab', 'datasources'), datasources_menu.focus();
                        if (val === 'Cancel') aggregation_menu.close(), auto_combo_menu.select();
                    })
                    .on('click', '.og-menu-datasources button', function () {
                        var val = $(this).text();
                        if (val === 'OK') $(selector).trigger('tab', 'load'), $(selector + ' .og-load').focus();
                        if (val === 'Cancel') datasources_menu.close(), auto_combo_menu.select();
                    });
                var auto_combo_menu = new og.common.util.ui.AutoCombo(
                        '.OG-analytics-form .og-view', 'search...', search.data)
                    .on('input autocompletechange autocompleteselect', function (event, ui) {
                        var $load = $(selector + ' .og-load');
                        if ((ui && ui.item && ui.item.value || $(this).val()) !== '') {
                            $load.removeClass('og-disabled').on('click', function () {
                                status.play();
                            });
                        }
                        else $load.addClass('og-disabled').off('click');
                    });
                var aggregation_menu = new FormCombo(
                    selector + ' .og-aggregation', 'og.analytics.form_aggregation_tash', aggregators.data
                );
                var datasources_menu = new FormCombo(
                    selector + ' .og-datasources', 'og.analytics.form_datasources_tash', ds_response
                );
                var status = new Status(selector + ' .og-status');
                og.views.common.layout.main.allowOverflow('north');
            };
            handle_error = function(err) {
                console.log(err);
            };
            $.when(
                og.api.text({module: 'og.analytics.form_tash'}),
                og.api.rest.viewdefinitions.get(),
                og.api.rest.aggregators.get()
            ).then(create_form);
        };
    }
});