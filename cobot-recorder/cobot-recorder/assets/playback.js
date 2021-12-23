! function(e, t) {
    if ("object" == typeof exports && "object" == typeof module) module.exports = t();
    else if ("function" == typeof define && define.amd) define([], t);
    else {
        var o = t();
        for (var n in o)("object" == typeof exports ? exports : e)[n] = o[n]
    }
}("undefined" != typeof self ? self : this, function() {
    return webpackJsonp([1], {
        163: function(e, t, o) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            });
            var n = o(15),
                r = o.n(n),
                i = (o(90), o(413)),
                s = o(254),
                l = o(255),
                a = o(257),
                u = o(416),
                c = o(417);
            const d = new s["a"](l["b"].createForWindow(window, !0));
            t["selenium"] = d;
            const p = new a["a"](window);
            let h, f = window.contentSideexTabId;

            function m(e, t, o) {
                if (e.commands) {
                    if ("waitPreparation" == e.commands) d["doWaitPreparation"]("", d.preprocessParameter("")), o({});
                    else if ("prePageWait" == e.commands) d["doPrePageWait"]("", d.preprocessParameter("")), o({
                        new_page: window.sideex_new_page
                    });
                    else if ("pageWait" == e.commands) d["doPageWait"]("", d.preprocessParameter("")), o({
                        page_done: window.sideex_page_done
                    });
                    else if ("ajaxWait" == e.commands) d["doAjaxWait"]("", d.preprocessParameter("")), o({
                        ajax_done: window.sideex_ajax_done
                    });
                    else if ("domWait" == e.commands) d["doDomWait"]("", d.preprocessParameter("")), o({
                        dom_time: window.sideex_new_page
                    });
                    else if ("evaluateConditional" === e.commands) try {
                        let t = d["doEvaluateConditional"](e.target);
                        o({
                            result: "success",
                            value: t
                        })
                    } catch (e) {
                        o({
                            result: e.message
                        })
                    } else {
                        const t = e.commands.charAt(0).toUpperCase() + e.commands.slice(1);
                        if (null != d["do" + t]) try {
                            document.body.setAttribute("SideeXPlayingFlag", !0);
                            let n = d["do" + t](d.preprocessParameter(e.target), d.preprocessParameter(e.value));
                            n instanceof Promise ? n.then(function() {
                                document.body.removeAttribute("SideeXPlayingFlag"), o({
                                    result: "success"
                                })
                            }).catch(function(e) {
                                document.body.removeAttribute("SideeXPlayingFlag"), o({
                                    result: e
                                })
                            }) : (document.body.removeAttribute("SideeXPlayingFlag"), o({
                                result: "success"
                            }))
                        } catch (e) {
                            document.body.removeAttribute("SideeXPlayingFlag"), o({
                                result: e.message
                            })
                        } else o({
                            result: "Unknown command: " + e.commands
                        })
                    }
                    return -1 === f && (f = e.mySideexTabId), !0
                }
                if (e.prepareToInteract && o({
                        result: "success",
                        rect: d.prepareToInteract_(e.locator)
                    }), e.buildLocators) try {
                    const t = d.browserbot.findElement(e.locator),
                        n = p.buildAll(t);
                    o({
                        result: "success",
                        locators: n
                    })
                } catch (e) {
                    o({
                        result: e.message
                    })
                }
                if (e.resolveLocator) try {
                    const t = d.browserbot.findElement(e.locator),
                        n = p.buildAll(t).find(([e, t]) => /^xpath/.test(t))[0];
                    o({
                        result: "success",
                        locator: n
                    })
                } catch (e) {
                    o({
                        result: e.message
                    })
                }
                if (e.selectMode)
                    if (o(!0), e.selecting && e.element) h = new i["a"](function(t, o) {
                        if (t && o) {
                            const o = p.buildAll(t);
                            p.detach(), null != o && o instanceof Array && o && r.a.runtime.sendMessage({
                                selectTarget: !0,
                                target: o,
                                selectNext: e.selectNext
                            })
                        }
                        h = null
                    }, function() {
                        r.a.runtime.sendMessage({
                            cancelSelectTarget: !0
                        })
                    });
                    else {
                        if (!e.selecting || !e.region) return h ? (h.cleanup(), void(h = null)) : void Object(u["b"])();
                        Object(u["a"])(e.rect, t => {
                            t ? r.a.runtime.sendMessage({
                                selectTarget: !0,
                                target: [
                                    [t]
                                ],
                                selectNext: e.selectNext
                            }) : r.a.runtime.sendMessage({
                                cancelSelectTarget: !0,
                                selectNext: e.selectNext
                            })
                        })
                    }
            }
            Object(c["a"])(d), window._listener || (window._listener = m, r.a.runtime.onMessage.addListener(function(e) {
                if (e.showElement) try {
                    const t = d["doShowElement"](e.targetValue);
                    return Promise.resolve({
                        result: t
                    })
                } catch (e) {}
            }), r.a.runtime.onMessage.addListener(m))
        },
        174: function(e, t, o) {
            "use strict";
            var n = o(175);

            function r(e) {
                return e === Object(e) && 0 !== Object.keys(e).length
            }
            t["a"] = function(e, t) {
                var o = !e.ownerDocument.documentElement.contains(e);
                if (r(t) && "function" == typeof t.behavior) return t.behavior(o ? [] : Object(n["a"])(e, t));
                if (!o) {
                    var i = function(e) {
                        return !1 === e ? {
                            block: "end",
                            inline: "nearest"
                        } : r(e) ? e : {
                            block: "start",
                            inline: "nearest"
                        }
                    }(t);
                    return function(e, t) {
                        void 0 === t && (t = "auto");
                        var o = "scrollBehavior" in document.body.style;
                        e.forEach(function(e) {
                            var n = e.el,
                                r = e.top,
                                i = e.left;
                            n.scroll && o ? n.scroll({
                                top: r,
                                left: i,
                                behavior: t
                            }) : (n.scrollTop = r, n.scrollLeft = i)
                        })
                    }(Object(n["a"])(e, i), i.behavior)
                }
            }
        },
        175: function(e, t, o) {
            "use strict";

            function n(e) {
                return null != e && "object" == typeof e && 1 === e.nodeType
            }

            function r(e, t) {
                return (!t || "hidden" !== e) && ("visible" !== e && "clip" !== e)
            }

            function i(e, t) {
                if (e.clientHeight < e.scrollHeight || e.clientWidth < e.scrollWidth) {
                    var o = getComputedStyle(e, null);
                    return r(o.overflowY, t) || r(o.overflowX, t)
                }
                return !1
            }

            function s(e, t, o, n, r, i, s, l) {
                return i < e && s > t || i > e && s < t ? 0 : i <= e && l <= o || s >= t && l >= o ? i - e - n : s > t && l < o || i < e && l > o ? s - t + r : 0
            }
            t["a"] = function(e, t) {
                var o = t.scrollMode,
                    r = t.block,
                    l = t.inline,
                    a = t.boundary,
                    u = t.skipOverflowHiddenElements,
                    c = "function" == typeof a ? a : function(e) {
                        return e !== a
                    };
                if (!n(e)) throw new TypeError("Invalid target");
                for (var d = document.scrollingElement || document.documentElement, p = [], h = e; n(h) && c(h);) {
                    if ((h = h.parentNode) === d) {
                        p.push(h);
                        break
                    }
                    h === document.body && i(h) && !i(document.documentElement) || i(h, u) && p.push(h)
                }
                for (var f = window.visualViewport ? visualViewport.width : innerWidth, m = window.visualViewport ? visualViewport.height : innerHeight, w = window.scrollX || pageXOffset, g = window.scrollY || pageYOffset, y = e.getBoundingClientRect(), b = y.height, E = y.width, v = y.top, x = y.right, C = y.bottom, T = y.left, S = "start" === r || "nearest" === r ? v : "end" === r ? C : v + b / 2, M = "center" === l ? T + E / 2 : "end" === l ? x : T, W = [], N = 0; N < p.length; N++) {
                    var P = p[N],
                        A = P.getBoundingClientRect(),
                        D = A.height,
                        _ = A.width,
                        L = A.top,
                        k = A.right,
                        I = A.bottom,
                        O = A.left;
                    if ("if-needed" === o && v >= 0 && T >= 0 && C <= m && x <= f && v >= L && C <= I && T >= O && x <= k) return W;
                    var R = getComputedStyle(P),
                        F = parseInt(R.borderLeftWidth, 10),
                        B = parseInt(R.borderTopWidth, 10),
                        X = parseInt(R.borderRightWidth, 10),
                        j = parseInt(R.borderBottomWidth, 10),
                        U = 0,
                        V = 0,
                        K = "offsetWidth" in P ? P.offsetWidth - P.clientWidth - F - X : 0,
                        $ = "offsetHeight" in P ? P.offsetHeight - P.clientHeight - B - j : 0;
                    if (d === P) U = "start" === r ? S : "end" === r ? S - m : "nearest" === r ? s(g, g + m, m, B, j, g + S, g + S + b, b) : S - m / 2, V = "start" === l ? M : "center" === l ? M - f / 2 : "end" === l ? M - f : s(w, w + f, f, F, X, w + M, w + M + E, E), U = Math.max(0, U + g), V = Math.max(0, V + w);
                    else {
                        U = "start" === r ? S - L - B : "end" === r ? S - I + j + $ : "nearest" === r ? s(L, I, D, B, j + $, S, S + b, b) : S - (L + D / 2) + $ / 2, V = "start" === l ? M - O - F : "center" === l ? M - (O + _ / 2) + K / 2 : "end" === l ? M - k + X + K : s(O, k, _, F, X + K, M, M + E, E);
                        var H = P.scrollLeft,
                            Y = P.scrollTop;
                        S += Y - (U = Math.max(0, Math.min(Y + U, P.scrollHeight - D + $))), M += H - (V = Math.max(0, Math.min(H + V, P.scrollWidth - _ + K)))
                    }
                    W.push({
                        el: P,
                        top: U,
                        left: V
                    })
                }
                return W
            }
        },
        176: function(e, t, o) {
            "use strict";

            function n(e) {
                return e.replace(/&amp;/gi, "&").replace(/&quot;/gi, '"').replace(/&lt;/gi, "<").replace(/&gt;/gi, ">").replace(/&#39;/gi, "'")
            }

            function r(e) {
                let t = 0,
                    o = -1,
                    n = e,
                    r = "",
                    i = "",
                    s = "",
                    a = !1,
                    u = !1;
                do {
                    if (t = e.indexOf(" "), (o = e.indexOf(" ", t + 1)) >= 0)
                        for (;
                            "'" != e.charAt(o - 1) && '"' != e.charAt(o - 1) && !((o = e.indexOf(" ", o + 1)) < 0););
                    if (t >= 0 && o >= 0) r = e.substring(t + 1, o), n = e.substring(0, t + 1), e = e.substring(o);
                    else {
                        if (!(t >= 0 && o < 0)) {
                            a ? s += ">" : s = e, u = !0;
                            break
                        }
                        r = e.substring(t + 1, e.length - 1), n = e.substring(0, t + 1), e = ""
                    }
                    a = !0;
                    let c = r.indexOf("=");
                    if ("'" == r.charAt(c + 1) && -1 != r.indexOf("'")) {
                        let e = r.indexOf("'"),
                            t = r.lastIndexOf("'");
                        i = r.substring(e + 1, t), r = r.substring(0, e + 1), r += (i = l(i)) + "'"
                    }
                    if ('"' == r.charAt(c + 1) && -1 != r.indexOf('"')) {
                        let e = r.indexOf('"'),
                            t = r.lastIndexOf('"');
                        i = r.substring(e + 1, t), r = r.substring(0, e + 1), r += (i = l(i)) + '"'
                    }
                    s += n + r
                } while (!u);
                return s
            }

            function i(e) {
                return e.replace(/[&"'<>]/g, e => ({
                    "&": "&amp;",
                    '"': "&quot;",
                    "'": "&#39;",
                    "<": "&lt;",
                    ">": "&gt;"
                })[e])
            }

            function s(e, t, o) {
                switch (o) {
                    case 1:
                        return e + (t + "&amp;");
                    case 2:
                        return e + (t + "&quot;");
                    case 3:
                        return e + (t + "&#39;");
                    case 4:
                        return e + (t + "&lt;");
                    case 5:
                        return e + (t + "&gt;");
                    default:
                        return e
                }
            }

            function l(e) {
                let t, o = -1,
                    n = "",
                    r = "",
                    l = 0,
                    a = !0;
                for (; a;) t = 0, -1 != (o = e.indexOf("&", o + 1)) ? ("&amp;" == e.substring(o, o + 5) ? (t = 1, r = e.substring(0, o), e = e.substring(o + 5)) : "&quot;" == e.substring(o, o + 6) ? (t = 2, r = e.substring(0, o), e = e.substring(o + 6)) : "&#39;" == e.substring(o, o + 5) ? (t = 3, r = e.substring(0, o), e = e.substring(o + 5)) : "&lt;" == e.substring(o, o + 4) ? (t = 4, r = e.substring(0, o), e = e.substring(o + 4)) : "&gt;" == e.substring(o, o + 4) && (t = 5, r = e.substring(0, o), e = e.substring(o + 4)), 0 != t && (o = -1, n = s(n, r = i(r), t), l = 1)) : (n += e, a = !1);
                return 0 == l ? i(e) : n
            }

            function a(e) {
                let t = e.indexOf("<"),
                    o = e.indexOf(">"),
                    n = "",
                    i = "",
                    s = "",
                    a = 0,
                    u = !0;
                for (; u;) {
                    if (!(t >= 0)) {
                        l(e), u = !1;
                        break
                    }
                    if (!(o >= 0)) {
                        l(e), u = !1;
                        break
                    }
                    do {
                        t += a, n = e.substring(0, t), a = (i = e.substring(t, o + 1)).lastIndexOf("<")
                    } while (0 != a);
                    i = r(i), e = e.substring(o + 1), s += l(n) + i, t = e.indexOf("<"), o = 0;
                    do {
                        o = e.indexOf(">", o + 1)
                    } while (o < t && -1 != o)
                }
                return "" != e && (s += l(e)), s
            }
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t["unescapeHtml"] = n, t["escapeHTML"] = a, window.unescapeHtml = n, window.escapeHTML = a
        },
        254: function(e, t, o) {
            "use strict";
            var n = o(15),
                r = o.n(n),
                i = o(174),
                s = o(129),
                l = o(163),
                a = o(176),
                u = o(255),
                c = o(90),
                d = o(256),
                p = o(130);
            class h {
                constructor(e) {
                    this.browserbot = e, this.optionLocatorFactory = new b, this.page = function() {
                        return e
                    }, this.defaultTimeout = h.DEFAULT_TIMEOUT, this.mouseSpeed = h.DEFAULT_MOUSE_SPEED
                }
            }

            function f(e) {
                if (!e) throw new Error("No variable name provided.")
            }

            function m(e, t, o, n) {
                if (!o) throw new Error("Timeout not specified.");
                return new Promise(function(r, i) {
                    let s, l = 0,
                        a = 100,
                        u = setInterval(function() {
                            l > o && (clearInterval(u), i(n));
                            try {
                                s = e(t)
                            } catch (e) {
                                clearInterval(u), i(e.message)
                            }
                            s ? s && (clearInterval(u), r()) : l += a
                        }, a)
                })
            }

            function w(e, t) {
                let o, n, r = null;
                return t ? (r = t.split(/,/), o = Number(r[0]), n = Number(r[1])) : o = n = 0, [h.prototype.getElementPositionLeft(e) + o, h.prototype.getElementPositionTop(e) + n]
            }

            function g(e, t) {
                let o, n, r = null;
                return t ? (r = t.split(/,/), o = Number(r[0]), n = Number(r[1])) : o = n = 0, new c["c"].math.Coordinate(o, n)
            }

            function y() {
                throw new Error("Unable to locate target element.")
            }

            function b() {}
            t["a"] = h, h.DEFAULT_TIMEOUT = 30 * 1e3, h.DEFAULT_MOUSE_SPEED = 10, h.RIGHT_MOUSE_CLICK = 2, h.decorateFunctionWithTimeout = function(e, t, o) {
                if (null == e) return null;
                let n = Object(p["d"])(t);
                return function() {
                    if ((new Date).getTime() > n) throw null != o && o(), new s["a"]("Timed out after " + t + "ms");
                    return e()
                }
            }, h.createForWindow = function(e, t) {
                if (!e.location) throw "error: not a window!";
                return h(u["b"].createForWindow(e, t))
            }, h.prototype.reset = function() {
                this.defaultTimeout = h.DEFAULT_TIMEOUT, this.browserbot.selectWindow("null"), this.browserbot.resetPopups()
            }, h.prototype.eval = function(e, t = [], o = !0, n = !1) {
                return n ? window.eval(o ? `((...arguments) => (${e}))(...JSON.parse('${JSON.stringify(t)}'))` : e) : window.eval(o ? `((...arguments) => {${e}})(...JSON.parse('${JSON.stringify(t)}'))` : e)
            }, h.prototype.doEvaluateConditional = function(e) {
                return !!this.eval(e.script, e.argv, !0, !0)
            }, h.prototype.doVerifyChecked = function(e) {
                let t = this.browserbot.findElement(e);
                if ("checkbox" !== t.type && "radio" !== t.type) throw new Error(`Element with locator ${e} is not a checkbox nor a radio button`);
                if (!t.checked) throw new Error(`Element with locator ${e} is not checked`)
            }, h.prototype.doVerifyNotChecked = function(e) {
                let t = this.browserbot.findElement(e);
                if ("checkbox" !== t.type && "radio" !== t.type) throw new Error(`Element with locator ${e} is not a checkbox nor a radio button`);
                if (t.checked) throw new Error(`Element with locator ${e} is checked`)
            }, h.prototype.doVerifyEditable = function(e) {
                if (!this.isEditable(e)) throw new Error(`Element with locator ${e} is not editable`)
            }, h.prototype.doVerifyNotEditable = function(e) {
                if (this.isEditable(e)) throw new Error(`Element with locator ${e} is editable`)
            }, h.prototype.doVerifySelectedValue = function(e, t) {
                let o = this.browserbot.findElement(e);
                if ("select-one" !== o.type) throw new Error(`Element with locator ${e} is not a select`);
                if (o.value !== t) throw new Error("Actual value '" + o.value + "' did not match '" + t + "'")
            }, h.prototype.doVerifyNotSelectedValue = function(e, t) {
                let o = this.browserbot.findElement(e);
                if ("select-one" !== o.type) throw new Error(`Element with locator ${e} is not a select`);
                if (o.value === t) throw new Error("Actual value '" + o.value + "' did match")
            }, h.prototype.doVerifyText = function(e, t) {
                this.doAssertText(e, t)
            }, h.prototype.doVerifyNotText = function(e, t) {
                this.doAssertNotText(e, t)
            }, h.prototype.doVerifyValue = function(e, t) {
                let o = this.browserbot.findElement(e);
                if (o.value !== t) throw new Error("Actual value '" + o.value + "' did not match '" + t + "'")
            }, h.prototype.doVerifyTitle = function(e) {
                if (c["c"].string.normalizeSpaces(this.getTitle()) !== e) throw new Error("Actual value '" + c["c"].string.normalizeSpaces(this.getTitle()) + "' did not match '" + e + "'")
            }, h.prototype.doVerifyElementPresent = function(e) {
                try {
                    this.browserbot.findElement(e)
                } catch (t) {
                    throw t.message.match(/Element[\s\S]*?not found/) ? new Error(`Element with locator ${e} could not be found`) : t
                }
            }, h.prototype.doVerifyElementNotPresent = function(e) {
                try {
                    throw this.browserbot.findElement(e), new Error(`Element with locator ${e} was found`)
                } catch (e) {
                    if (!e.message.match(/Element[\s\S]*?not found/)) throw e
                }
            }, h.prototype.doVerify = function(e, t) {
                return this.doAssert(e, t)
            }, h.prototype.doAssert = function(e, t) {
                return new Promise((o, n) => {
                    r.a.runtime.sendMessage({
                        getVar: !0,
                        variable: e
                    }).then(e => `${e}` != t ? n("Actual value '" + e + "' did not match '" + t + "'") : o())
                })
            }, h.prototype.doAssertChecked = function(e) {
                let t = this.browserbot.findElement(e);
                if ("checkbox" !== t.type && "radio" !== t.type) throw new Error(`Element with locator ${e} is not a checkbox nor a radio button`);
                if (!t.checked) throw new Error(`Element with locator ${e} is not checked`)
            }, h.prototype.doAssertNotChecked = function(e) {
                let t = this.browserbot.findElement(e);
                if ("checkbox" !== t.type && "radio" !== t.type) throw new Error(`Element with locator ${e} is not a checkbox nor a radio button`);
                if (t.checked) throw new Error(`Element with locator ${e} is checked`)
            }, h.prototype.doAssertEditable = function(e) {
                if (!this.isEditable(e)) throw new Error(`Element with locator ${e} is not editable`)
            }, h.prototype.doAssertNotEditable = function(e) {
                if (this.isEditable(e)) throw new Error(`Element with locator ${e} is editable`)
            }, h.prototype.doAssertSelectedValue = function(e, t) {
                let o = this.browserbot.findElement(e);
                if ("select-one" !== o.type) throw new Error(`Element with locator ${e} is not a select`);
                if (o.value !== t) throw new Error("Actual value '" + o.value + "' did not match '" + t + "'")
            }, h.prototype.doVerifySelectedLabel = function(e, t) {
                let o = this.findSelectedOptionProperty(e, "text");
                if (o !== t) throw new Error("Actual label '" + o + "' did not match '" + t + "'")
            }, h.prototype.doAssertSelectedLabel = function(e, t) {
                let o = this.findSelectedOptionProperty(e, "text");
                if (o !== t) throw new Error("Actual label '" + o + "' did not match '" + t + "'")
            }, h.prototype.doAssertNotSelectedValue = function(e, t) {
                let o = this.browserbot.findElement(e);
                if ("select-one" !== o.type) throw new Error(`Element with locator ${e} is not a select`);
                if (o.value === t) throw new Error("Actual value '" + o.value + "' did match")
            }, h.prototype.findElementVisible = function(e) {
                const t = this.browserbot.findElement(e);
                if (!c["a"].dom.isShown(t)) throw new Error(`Element ${e} not visible`);
                return t
            }, h.prototype.doAssertText = function(e, t) {
                const o = this.findElementVisible(e),
                    n = c["a"].dom.getVisibleText(o).trim();
                if (n !== t) throw new Error(`Actual value "${n}" did not match "${t}"`)
            }, h.prototype.doAssertNotText = function(e, t) {
                const o = this.findElementVisible(e),
                    n = c["a"].dom.getVisibleText(o).trim();
                if (n === t) throw new Error(`Actual value "${n}" did match "${t}"`)
            }, h.prototype.doAssertValue = function(e, t) {
                let o = this.browserbot.findElement(e);
                if (o.value !== t) throw new Error("Actual value '" + o.value + "' did not match '" + t + "'")
            }, h.prototype.doAssertTitle = function(e) {
                if (c["c"].string.normalizeSpaces(this.getTitle()) !== e) throw new Error("Actual value '" + c["c"].string.normalizeSpaces(this.getTitle()) + "' did not match '" + e + "'")
            }, h.prototype.doAssertElementPresent = function(e) {
                try {
                    this.browserbot.findElement(e)
                } catch (t) {
                    throw t.message.match(/Element[\s\S]*?not found/) ? new Error(`Element with locator ${e} could not be found`) : t
                }
            }, h.prototype.doAssertElementNotPresent = function(e) {
                try {
                    throw this.browserbot.findElement(e), new Error(`Element with locator ${e} was found`)
                } catch (e) {
                    if (!e.message.match(/Element[\s\S]*?not found/)) throw e
                }
            }, h.prototype.doStore = function(e, t) {
                return f(t), r.a.runtime.sendMessage({
                    storeStr: e,
                    storeVar: t
                })
            }, h.prototype.doStoreEval = function() {
                throw new Error("store eval is obsolete please migrate to execute script")
            }, h.prototype.doStoreJson = function(e, t) {
                f(t);
                const o = JSON.parse(e);
                return r.a.runtime.sendMessage({
                    storeStr: o,
                    storeVar: t
                })
            }, h.prototype.doStoreText = function(e, t) {
                let o;
                f(t);
                try {
                    const t = this.findElementVisible(e);
                    o = c["a"].dom.getVisibleText(t).trim()
                } catch (t) {
                    if ("" !== this.browserbot.findElement(e).innerHTML) throw t;
                    o = ""
                }
                return r.a.runtime.sendMessage({
                    storeStr: o,
                    storeVar: t
                })
            }, h.prototype.doStoreValue = function(e, t) {
                f(t);
                let o = this.browserbot.findElement(e);
                return r.a.runtime.sendMessage({
                    storeStr: o.value.trim(),
                    storeVar: t
                })
            }, h.prototype.doStoreTitle = function(e, t) {
                f(t);
                let o = l["selenium"].browserbot.getDocument();
                return r.a.runtime.sendMessage({
                    storeStr: e || o.title,
                    storeVar: t
                })
            }, h.prototype.doStoreXpathCount = function(e, t) {
                f(t);
                let o = this.browserbot.evaluateXPathCount(e, this.browserbot.getDocument());
                return r.a.runtime.sendMessage({
                    storeStr: `${o}` || "0",
                    storeVar: t
                })
            }, h.prototype.doStoreAttribute = function(e, t) {
                f(t);
                let o = this.browserbot.findAttribute(e);
                return r.a.runtime.sendMessage({
                    storeStr: o,
                    storeVar: t
                })
            }, h.prototype.doWaitForElementPresent = function(e, t) {
                return m(this.isElementPresent.bind(this), e, t, "Unable to find the target element within the timeout specified.")
            }, h.prototype.doWaitForElementNotPresent = function(e, t) {
                return m(function(e) {
                    return !this.isElementPresent(e)
                }.bind(this), e, t, "Element still present on the page within the timeout specified.")
            }, h.prototype.doWaitForElementVisible = function(e, t) {
                return m(function(e) {
                    try {
                        return this.isVisible(e)
                    } catch (e) {
                        return !1
                    }
                }.bind(this), e, t, "Element not visible on the page within the timeout specified.")
            }, h.prototype.doWaitForElementNotVisible = function(e, t) {
                return m(function(e) {
                    try {
                        return !this.isVisible(e)
                    } catch (e) {
                        y()
                    }
                }.bind(this), e, t, "Element still visible on the page within the timeout specified.")
            }, h.prototype.doWaitForElementEditable = function(e, t) {
                return m(function(e) {
                    try {
                        return this.isEditable(e)
                    } catch (e) {
                        y()
                    }
                }.bind(this), e, t, "Element not editable within the timeout specified.")
            }, h.prototype.doWaitForElementNotEditable = function(e, t) {
                return m(function(e) {
                    try {
                        return !this.isEditable(e)
                    } catch (e) {
                        y()
                    }
                }.bind(this), e, t, "Element still editable within the timeout specified.")
            }, h.prototype.doWaitPreparation = function() {
                this.eval('function setNewPageValue(e) {window.new_page = true;};                window.addEventListener("beforeunload", setNewPageValue, false);                if (window.XMLHttpRequest) {if (!window.origXMLHttpRequest || !window.ajax_obj) {                window.ajax_obj = []; window.origXMLHttpRequest = window.XMLHttpRequest;                window.XMLHttpRequest = function() { var xhr = new window.origXMLHttpRequest();                window.ajax_obj.push(xhr); return xhr;}}} function setDOMModifiedTime() {                window.domModifiedTime = Date.now();}var _win = window.document.body;                _win.addEventListener("DOMNodeInserted", setDOMModifiedTime, false);                _win.addEventListener("DOMNodeInsertedIntoDocument", setDOMModifiedTime, false);                _win.addEventListener("DOMNodeRemoved", setDOMModifiedTime, false);                _win.addEventListener("DOMNodeRemovedFromDocument", setDOMModifiedTime, false);                _win.addEventListener("DOMSubtreeModified", setDOMModifiedTime, false);', [], !1)
            }, h.prototype.doPrePageWait = function() {
                window.sideex_new_page = this.eval("(function() {return window.new_page;}())", [], !1)
            }, h.prototype.doPageWait = function() {
                window.sideex_page_done = this.eval("(function() {" + 'if(window.document.readyState=="complete"){return true;}else{return false;}' + "}())", [], !1)
            }, h.prototype.doAjaxWait = function() {
                window.sideex_ajax_done = this.eval("(function() {" + 'if (window.ajax_obj) { if (window.ajax_obj.length == 0) {return true;} else {                      for (var index in window.ajax_obj) {                      if (window.ajax_obj[index].readyState !== 4 &&                      window.ajax_obj[index].readyState !== undefined &&                      window.ajax_obj[index].readyState !== 0) {return false;}}return true;}}                      else {if (window.origXMLHttpRequest) {window.origXMLHttpRequest = "";}return true;}' + "}())", [], !1)
            }, h.prototype.doDomWait = function() {
                window.sideex_dom_time = this.eval("(function() {return window.domModifiedTime;}())", [], !1)
            }, h.prototype.doClick = function(e) {
                let t = this.browserbot.findElement(e);
                c["a"].action.click(t)
            }, h.prototype.doDoubleClick = function(e) {
                let t = this.browserbot.findElement(e);
                c["a"].action.doubleClick(t)
            }, h.prototype.doContextMenu = function(e) {
                let t = this.browserbot.findElement(e);
                c["a"].action.rightClick(t)
            }, h.prototype.doClickAt = function(e, t) {
                let o = this.browserbot.findElement(e),
                    n = g(o, t);
                c["a"].action.click(o, n)
            }, h.prototype.doDoubleClickAt = function(e, t) {
                let o = this.browserbot.findElement(e),
                    n = g(o, t);
                c["a"].action.doubleClick(o, n)
            }, h.prototype.doContextMenuAt = function(e, t) {
                let o = this.browserbot.findElement(e),
                    n = g(o, t);
                c["a"].action.rightClick(o, n)
            }, h.prototype.doFocus = function(e) {
                let t = this.browserbot.findElement(e);
                t.focus ? t.focus() : c["a"].events.fire(t, c["a"].events.EventType.FOCUS)
            }, h.prototype.doShiftKeyDown = function() {
                this.browserbot.shiftKeyDown = !0, c["b"].events.shiftKeyDown_ = !0
            }, h.prototype.doShiftKeyUp = function() {
                this.browserbot.shiftKeyDown = !1, c["b"].events.shiftKeyDown_ = !1
            }, h.prototype.doMetaKeyDown = function() {
                this.browserbot.metaKeyDown = !0, c["b"].events.metaKeyDown_ = !0
            }, h.prototype.doMetaKeyUp = function() {
                this.browserbot.metaKeyDown = !1, c["b"].events.metaKeyDown_ = !1
            }, h.prototype.doAltKeyDown = function() {
                this.browserbot.altKeyDown = !0, c["b"].events.altKeyDown_ = !0
            }, h.prototype.doAltKeyUp = function() {
                this.browserbot.altKeyDown = !1, c["b"].events.altKeyDown_ = !1
            }, h.prototype.doControlKeyDown = function() {
                this.browserbot.controlKeyDown = !0, c["b"].events.controlKeyDown_ = !0
            }, h.prototype.doControlKeyUp = function() {
                this.browserbot.controlKeyDown = !1, c["b"].events.controlKeyDown_ = !1
            }, h.prototype.prepareToInteract_ = function(e) {
                let t = this.browserbot.findElement(e),
                    o = t.getBoundingClientRect();
                return c["a"].action.prepareToInteractWith_(t, new c["c"].math.Coordinate(o.width / 2, o.height / 2)), t.getBoundingClientRect()
            }, h.prototype.doMouseOver = function(e) {
                let t = this.browserbot.findElement(e),
                    o = t.getBoundingClientRect();
                c["a"].action.moveMouse(t, new c["c"].math.Coordinate(o.width / 2, o.height / 2))
            }, h.prototype.doMouseOut = function(e) {
                let t = this.browserbot.findElement(e);
                this.browserbot.triggerMouseEvent(t, "mouseout", !0)
            }, h.prototype.doMouseDown = function(e) {
                let t = this.browserbot.findElement(e);
                this.browserbot.triggerMouseEvent(t, "mousedown", !0)
            }, h.prototype.doMouseDownRight = function(e) {
                let t = this.browserbot.findElement(e);
                this.browserbot.triggerMouseEvent(t, "mousedown", !0, void 0, void 0, h.RIGHT_MOUSE_CLICK)
            }, h.prototype.doMouseDownAt = function(e, t) {
                let o = this.browserbot.findElement(e),
                    n = w(o, t);
                this.browserbot.triggerMouseEvent(o, "mousedown", !0, n[0], n[1])
            }, h.prototype.doMouseDownRightAt = function(e, t) {
                let o = this.browserbot.findElement(e),
                    n = w(o, t);
                this.browserbot.triggerMouseEvent(o, "mousedown", !0, n[0], n[1], h.RIGHT_MOUSE_CLICK)
            }, h.prototype.doMouseUp = function(e) {
                let t = this.browserbot.findElement(e);
                this.browserbot.triggerMouseEvent(t, "mouseup", !0)
            }, h.prototype.doMouseUpRight = function(e) {
                let t = this.browserbot.findElement(e);
                this.browserbot.triggerMouseEvent(t, "mouseup", !0, void 0, void 0, h.RIGHT_MOUSE_CLICK)
            }, h.prototype.doMouseUpAt = function(e, t) {
                let o = this.browserbot.findElement(e),
                    n = w(o, t);
                this.browserbot.triggerMouseEvent(o, "mouseup", !0, n[0], n[1])
            }, h.prototype.doMouseUpRightAt = function(e, t) {
                let o = this.browserbot.findElement(e),
                    n = w(o, t);
                this.browserbot.triggerMouseEvent(o, "mouseup", !0, n[0], n[1], h.RIGHT_MOUSE_CLICK)
            }, h.prototype.doMouseMove = function(e) {
                let t = this.browserbot.findElement(e);
                this.browserbot.triggerMouseEvent(t, "mousemove", !0)
            }, h.prototype.doMouseMoveAt = function(e, t) {
                let o = this.browserbot.findElement(e),
                    n = w(o, t);
                this.browserbot.triggerMouseEvent(o, "mousemove", !0, n[0], n[1])
            }, h.prototype.doType = function(e, t) {
                if (this.browserbot.controlKeyDown || this.browserbot.altKeyDown || this.browserbot.metaKeyDown) throw new s["a"]("type not supported immediately after call to controlKeyDown() or altKeyDown() or metaKeyDown()");
                let o = this.browserbot.findElement(e);
                c["b"].events.setValue(o, "");
                const n = o.type;
                "number" === n || "date" === n ? c["b"].events.setValue(o, t) : (c["a"].action.type(o, t), o.value !== t && c["b"].events.setValue(o, t)), c["a"].events.fire(o, c["a"].events.EventType.CHANGE)
            }, h.prototype.doSendKeys = function(e, t) {
                if (this.browserbot.controlKeyDown || this.browserbot.altKeyDown || this.browserbot.metaKeyDown) throw new s["a"]("type not supported immediately after call to controlKeyDown() or altKeyDown() or metaKeyDown()");
                let o = this.browserbot.findElement(e),
                    n = this.replaceKeys(t);
                c["a"].action.type(o, n), c["a"].events.fire(o, c["a"].events.EventType.CHANGE)
            }, h.prototype.doSetSpeed = function() {
                throw new s["a"]("this operation is only implemented in selenium-rc, and should never result in a request making it across the wire")
            }, h.prototype.getSpeed = function() {
                throw new s["a"]("this operation is only implemented in selenium-rc, and should never result in a request making it across the wire")
            }, h.prototype.findToggleButton = function(e) {
                let t = this.browserbot.findElement(e);
                if (null == t.checked) throw new Error("Element " + e + " is not a toggle-button.");
                return t
            }, h.prototype.doCheck = function(e) {
                this.findToggleButton(e).checked = !0
            }, h.prototype.doUncheck = function(e) {
                this.findToggleButton(e).checked = !1
            }, h.prototype.doSelect = function(e, t) {
                let o = this.browserbot.findElement(e);
                if (!("options" in o)) throw new s["a"]("Specified element is not a Select (has no options)");
                let n = this.optionLocatorFactory.fromLocatorString(t).findOption(o);
                this.browserbot.selectOption(o, n)
            }, h.prototype.doAddSelection = function(e, t) {
                let o = this.browserbot.findElement(e);
                if (!("options" in o)) throw new s["a"]("Specified element is not a Select (has no options)");
                let n = this.optionLocatorFactory.fromLocatorString(t).findOption(o);
                this.browserbot.addSelection(o, n)
            }, h.prototype.doRemoveSelection = function(e, t) {
                let o = this.browserbot.findElement(e);
                if (!("options" in o)) throw new s["a"]("Specified element is not a Select (has no options)");
                let n = this.optionLocatorFactory.fromLocatorString(t).findOption(o);
                this.browserbot.removeSelection(o, n)
            }, h.prototype.doRemoveAllSelections = function(e) {
                let t = this.browserbot.findElement(e);
                if (!("options" in t)) throw new s["a"]("Specified element is not a Select (has no options)");
                for (let e = 0; e < t.options.length; e++) this.browserbot.removeSelection(t, t.options[e])
            }, h.prototype.doSubmit = function(e) {
                r.a.runtime.sendMessage({
                    log: {
                        type: "warn",
                        message: "submit is deprecated and will be removed from later versions of Selenium IDE, please re-record the step."
                    }
                });
                let t = this.browserbot.findElement(e);
                return c["a"].action.submit(t)
            }, h.prototype.makePageLoadCondition = function(e) {
                if (null == e && (e = this.defaultTimeout), 0 != e) return h.decorateFunctionWithTimeout(this._isNewPageLoaded.bind(this), e, this._abortXhrRequest.bind(this));
                this._abortXhrRequest()
            }, h.prototype.doOpen = function(e, t) {
                if (null == t || 0 == t.length ? this.browserbot.ignoreResponseCode = !0 : "true" == t.toLowerCase() ? this.browserbot.ignoreResponseCode = !0 : this.browserbot.ignoreResponseCode = !1, this.browserbot.openLocation(e), null == window["proxyInjectionMode"] || !window["proxyInjectionMode"]) return this.makePageLoadCondition()
            }, h.prototype.doOpenWindow = function(e, t) {
                this.browserbot.openWindow(e, t)
            }, h.prototype.doSelectWindow = function(e) {
                this.browserbot.selectWindow(e)
            }, h.prototype.doSelectPopUp = function(e) {
                this.browserbot.selectPopUp(e)
            }, h.prototype.doDeselectPopUp = function() {
                this.browserbot.selectWindow()
            }, h.prototype.doSelectFrame = function(e) {
                this.browserbot.selectFrame(e)
            }, h.prototype.getWhetherThisFrameMatchFrameExpression = function(e, t) {
                return this.browserbot.doesThisFrameMatchFrameExpression(e, t)
            }, h.prototype.getWhetherThisWindowMatchWindowExpression = function(e, t) {
                return null != window.opener && null != window.opener[t] && window.opener[t] == window
            }, h.prototype.doWaitForPopUp = function(e, t) {
                t || (t = this.defaultTimeout);
                let o = Object(p["d"])(t);
                return h.decorateFunctionWithTimeout(function() {
                    let t;
                    try {
                        if (e && "null" != e) t = l["selenium"].browserbot.getWindowByName(e, !0);
                        else {
                            let e = l["selenium"].browserbot.getNonTopWindowNames();
                            t = l["selenium"].browserbot.getWindowByName(e[0], !0)
                        }
                    } catch (e) {
                        if ((new Date).getTime() > o) throw e
                    }
                    if (!t) return !1;
                    try {
                        if (!t.location) return !1;
                        if ("about:blank" == t.location) return !1
                    } catch (e) {
                        return !1
                    }
                    return !(u["a"].isKonqueror && "/" == t.location.href || u["a"].isSafari && t.location.href == l["selenium"].browserbot.buttonWindow.location.href || !t.document || l["selenium"].browserbot.getCurrentWindow().document.readyState && "complete" != t.document.readyState)
                }, t)
            }, h.prototype.doWaitForPopUp.dontCheckAlertsAndConfirms = !0, h.prototype.doGoBack = function() {
                this.browserbot.goBack()
            }, h.prototype.doRefresh = function() {
                this.browserbot.refresh()
            }, h.prototype.doClose = function() {
                this.browserbot.close()
            }, h.prototype.ensureNoUnhandledPopups = function() {
                if (this.browserbot.hasAlerts()) throw new s["a"]("There was an unexpected Alert! [" + this.browserbot.getNextAlert() + "]");
                if (this.browserbot.hasConfirmations()) throw new s["a"]("There was an unexpected Confirmation! [" + this.browserbot.getNextConfirmation() + "]")
            }, h.prototype.isAlertPresent = function() {
                return this.browserbot.hasAlerts()
            }, h.prototype.isPromptPresent = function() {
                return this.browserbot.hasPrompts()
            }, h.prototype.isConfirmationPresent = function() {
                return this.browserbot.hasConfirmations()
            }, h.prototype.getAlert = function() {
                return this.browserbot.hasAlerts() || Assert.fail("There were no alerts"), this.browserbot.getNextAlert()
            }, h.prototype.getAlert.dontCheckAlertsAndConfirms = !0, h.prototype.getConfirmation = function() {
                return this.browserbot.hasConfirmations() || Assert.fail("There were no confirmations"), this.browserbot.getNextConfirmation()
            }, h.prototype.getConfirmation.dontCheckAlertsAndConfirms = !0, h.prototype.getPrompt = function() {
                return this.browserbot.hasPrompts() || Assert.fail("There were no prompts"), this.browserbot.getNextPrompt()
            }, h.prototype.getLocation = function() {
                return this.browserbot.getCurrentWindow().location.href
            }, h.prototype.getTitle = function() {
                return this.browserbot.getTitle()
            }, h.prototype.getBodyText = function() {
                return this.browserbot.bodyText()
            }, h.prototype.getValue = function(e) {
                return this.browserbot.findElement(e).value.trim()
            }, h.prototype.getText = function(e) {
                let t = this.browserbot.findElement(e);
                return c["a"].dom.getVisibleText(t).trim()
            }, h.prototype.getEval = function(e) {
                try {
                    let t = this.eval(e);
                    return null == t ? "null" : t
                } catch (e) {
                    throw new s["a"]("Threw an exception: " + Object(p["b"])(e))
                }
            }, h.prototype.isChecked = function(e) {
                let t = this.browserbot.findElement(e);
                if (null == t.checked) throw new s["a"]("Element " + e + " is not a toggle-button.");
                return t.checked
            }, h.prototype.getTable = function(e) {
                let t = /(.*)\.(\d+)\.(\d+)/;
                if (!t.test(e)) throw new s["a"]("Invalid target format. Correct format is tableName.rowNum.columnNum");
                let o = e.match(t),
                    n = o[1],
                    r = o[2],
                    i = o[3],
                    l = this.browserbot.findElement(n);
                if (r > l.rows.length) Assert.fail("Cannot access row " + r + " - table has " + l.rows.length + " rows");
                else {
                    if (!(i > l.rows[r].cells.length)) {
                        return c["a"].dom.getVisibleText(l.rows[r].cells[i]).trim()
                    }
                    Assert.fail("Cannot access column " + i + " - table row has " + l.rows[r].cells.length + " columns")
                }
                return null
            }, h.prototype.getSelectedLabels = function(e) {
                return this.findSelectedOptionProperties(e, "text")
            }, h.prototype.getSelectedLabel = function(e) {
                return this.findSelectedOptionProperty(e, "text")
            }, h.prototype.getSelectedValues = function(e) {
                return this.findSelectedOptionProperties(e, "value")
            }, h.prototype.getSelectedValue = function(e) {
                return this.findSelectedOptionProperty(e, "value")
            }, h.prototype.getSelectedIndexes = function(e) {
                return this.findSelectedOptionProperties(e, "index")
            }, h.prototype.getSelectedIndex = function(e) {
                return this.findSelectedOptionProperty(e, "index")
            }, h.prototype.getSelectedIds = function(e) {
                return this.findSelectedOptionProperties(e, "id")
            }, h.prototype.getSelectedId = function(e) {
                return this.findSelectedOptionProperty(e, "id")
            }, h.prototype.isSomethingSelected = function(e) {
                let t = this.browserbot.findElement(e);
                if (!("options" in t)) throw new s["a"]("Specified element is not a Select (has no options)");
                for (let e = 0; e < t.options.length; e++)
                    if (t.options[e].selected) return !0;
                return !1
            }, h.prototype.findSelectedOptionProperties = function(e, t) {
                let o = this.browserbot.findElement(e);
                if (!("options" in o)) throw new s["a"]("Specified element is not a Select (has no options)");
                let n = [];
                for (let e = 0; e < o.options.length; e++)
                    if (o.options[e].selected) {
                        let r = o.options[e][t];
                        n.push(r)
                    } return 0 == n.length && Assert.fail("No option selected"), n
            }, h.prototype.findSelectedOptionProperty = function(e, t) {
                let o = this.findSelectedOptionProperties(e, t);
                return o.length > 1 && Assert.fail("More than one selected option!"), o[0]
            }, h.prototype.getSelectOptions = function(e) {
                let t = this.browserbot.findElement(e),
                    o = [];
                for (let e = 0; e < t.options.length; e++) {
                    let n = t.options[e].text;
                    o.push(n)
                }
                return o
            }, h.prototype.getAttribute = function(e) {
                let t = this.browserbot.findAttribute(e);
                if (null == t) throw new s["a"]("Could not find element attribute: " + e);
                return t
            }, h.prototype.isTextPresent = function(e) {
                let t = this.browserbot.bodyText(),
                    o = new d["a"](e);
                if (o.strategy == d["a"].strategies.glob) 0 == e.indexOf("glob:") && (e = e.substring("glob:".length)), o.matcher = new d["a"].strategies.globContains(e);
                else if (o.strategy == d["a"].strategies.exact) return e = e.substring("exact:".length), -1 != t.indexOf(e);
                return o.matches(t)
            }, h.prototype.isElementPresent = function(e) {
                return null != this.browserbot.findElementOrNull(e)
            }, h.prototype.isVisible = function(e) {
                let t;
                if ((t = this.browserbot.findElement(e)).tagName) {
                    if ("input" == new String(t.tagName).toLowerCase() && t.type) {
                        if ("hidden" == new String(t.type).toLowerCase()) return !1
                    }
                }
                let o = this.findEffectiveStyleProperty(t, "visibility"),
                    n = this._isDisplayed(t);
                return "hidden" != o && n
            }, h.prototype.findEffectiveStyleProperty = function(e, t) {
                let o = this.findEffectiveStyle(e)[t];
                return "inherit" == o && e.parentNode.style ? this.findEffectiveStyleProperty(e.parentNode, t) : o
            }, h.prototype._isDisplayed = function(e) {
                return "none" != this.findEffectiveStyleProperty(e, "display") && (!e.parentNode.style || this._isDisplayed(e.parentNode))
            }, h.prototype.findEffectiveStyle = function(e) {
                if (void 0 == e.style) return;
                let t = this.browserbot.getCurrentWindow();
                if (t.getComputedStyle) return t.getComputedStyle(e, null);
                if (e.currentStyle) return e.currentStyle;
                if (t.document.defaultView && t.document.defaultView.getComputedStyle) return t.document.defaultView.getComputedStyle(e, null);
                throw new s["a"]("cannot determine effective stylesheet in this browser")
            }, h.prototype.isEditable = function(e) {
                let t = this.browserbot.findElement(e);
                if (void 0 == t.value && Assert.fail("Element " + e + " is not an input."), t.disabled) return !1;
                let o = t.getAttributeNode("readonly");
                if (o) {
                    if ("boolean" != typeof o.nodeValue) return !1;
                    if (o.nodeValue) return !1
                }
                return !0
            }, h.prototype.getAllButtons = function() {
                return this.browserbot.getAllButtons()
            }, h.prototype.getAllLinks = function() {
                return this.browserbot.getAllLinks()
            }, h.prototype.getAllFields = function() {
                return this.browserbot.getAllFields()
            }, h.prototype.doSetMouseSpeed = function(e) {
                let t = new Number(e);
                t.constructor != Number || t < 0 ? this.mouseSpeed = h.DEFAULT_MOUSE_SPEED : this.mouseSpeed = e
            }, h.prototype.getMouseSpeed = function() {
                return this.mouseSpeed
            }, h.prototype.doDragAndDrop = function(e, t) {
                let o = this.browserbot.findElement(e),
                    n = w(o),
                    r = n[0],
                    i = n[1],
                    s = t.split(/,/),
                    l = Number(s[0]),
                    a = Number(s[1]),
                    u = r + l < 0 ? 0 : r + l,
                    c = i + a < 0 ? 0 : i + a,
                    d = this.mouseSpeed,
                    p = function(e, t) {
                        return e == t ? e : Math.abs(e - t) < d ? t : e < t ? e + d : e - d
                    };
                this.browserbot.triggerMouseEvent(o, "mousedown", !0, r, i), this.browserbot.triggerMouseEvent(o, "mousemove", !0, r, i);
                let h = r,
                    f = i;
                for (; h != u || f != c;) h = p(h, u), f = p(f, c), this.browserbot.triggerMouseEvent(o, "mousemove", !0, h, f);
                this.browserbot.triggerMouseEvent(o, "mousemove", !0, u, c), this.browserbot.triggerMouseEvent(o, "mouseup", !0, u, c)
            }, h.prototype.doDragAndDropToObject = function(e, t) {
                if (this.browserbot.findElement(e).draggable) {
                    let o = this.browserbot.findElement(e),
                        n = this.browserbot.findElement(t);
                    this.browserbot.triggerDragEvent(o, n)
                } else {
                    let o = this.getElementPositionLeft(e),
                        n = this.getElementPositionTop(e),
                        r = this.getElementPositionLeft(t),
                        i = this.getElementPositionTop(t),
                        s = this.getElementWidth(t),
                        l = this.getElementHeight(t),
                        a = Math.round(r + s / 2) - o + "," + (Math.round(i + l / 2) - n);
                    this.doDragAndDrop(e, a)
                }
            }, h.prototype.doWindowFocus = function() {
                this.browserbot.getCurrentWindow().focus()
            }, h.prototype.doWindowMaximize = function() {
                let e = this.browserbot.getCurrentWindow();
                null != e && e.screen && (e.moveTo(0, 0), 0 != e.screenX && e.moveTo(0, 1), e.resizeTo(screen.availWidth, screen.availHeight))
            }, h.prototype.getHtmlSource = function() {
                return this.browserbot.getDocument().getElementsByTagName("html")[0].innerHTML
            }, h.prototype.doSetCursorPosition = function(e, t) {
                let o = this.browserbot.findElement(e);
                if (void 0 == o.value && Assert.fail("Element " + e + " is not an input."), -1 == t && (t = o.value.length), o.setSelectionRange && !u["a"].isOpera) o.focus(), o.setSelectionRange(t, t);
                else if (o.createTextRange) {
                    c["a"].events.fire(o, c["a"].events.EventType.FOCUS);
                    let e = o.createTextRange();
                    e.collapse(!0), e.moveEnd("character", t), e.moveStart("character", t), e.select()
                }
            }, h.prototype.getElementIndex = function(e) {
                let t, o = this.browserbot.findElement(e),
                    n = 0;
                for (; null != (t = o.previousSibling);) this._isCommentOrEmptyTextNode(t) || n++, o = t;
                return n
            }, h.prototype.isOrdered = function(e, t) {
                let o, n = this.browserbot.findElement(e),
                    r = this.browserbot.findElement(t);
                if (n === r) return !1;
                for (; null != (o = r.previousSibling);) {
                    if (o === n) return !0;
                    r = o
                }
                return !1
            }, h.prototype._isCommentOrEmptyTextNode = function(e) {
                return 8 == e.nodeType || 3 == e.nodeType && !/[^\t\n\r ]/.test(e.data)
            }, h.prototype.getElementPositionLeft = function(e) {
                let t, o = (t = "string" == typeof e ? this.browserbot.findElement(e) : e).offsetLeft,
                    n = t.offsetParent;
                for (; null != n;) {
                    if (document.all) "TABLE" != n.tagName && "BODY" != n.tagName && (o += n.clientLeft);
                    else if ("TABLE" == n.tagName) {
                        let e = parseInt(n.border);
                        if (isNaN(e)) {
                            null != n.getAttribute("frame") && (o += 1)
                        } else e > 0 && (o += e)
                    }
                    o += n.offsetLeft, n = n.offsetParent
                }
                return o
            }, h.prototype.getElementPositionTop = function(e) {
                let t;
                t = "string" == typeof e ? this.browserbot.findElement(e) : e;
                let o = 0;
                for (; null != t;) {
                    if (document.all) "TABLE" != t.tagName && "BODY" != t.tagName && (o += t.clientTop);
                    else if ("TABLE" == t.tagName) {
                        let e = parseInt(t.border);
                        if (isNaN(e)) {
                            null != t.getAttribute("frame") && (o += 1)
                        } else e > 0 && (o += e)
                    }
                    o += t.offsetTop, t = t.offsetParent && t.offsetParent.offsetHeight && t.offsetParent.offsetHeight < t.offsetHeight ? t.offsetParent.offsetParent : t.offsetParent
                }
                return o
            }, h.prototype.getElementWidth = function(e) {
                return this.browserbot.findElement(e).offsetWidth
            }, h.prototype.getElementHeight = function(e) {
                return this.browserbot.findElement(e).offsetHeight
            }, h.prototype.getCursorPosition = function(e) {
                let t, o = this.browserbot.findElement(e),
                    n = this.browserbot.getDocument(),
                    r = this.browserbot.getCurrentWindow();
                if (n.selection && !u["a"].isOpera) {
                    try {
                        let e = n.selection.createRange().duplicate();
                        t = o.createTextRange(), e.move("character", 0), t.move("character", 0), t.setEndPoint("EndToEnd", e)
                    } catch (e) {
                        Assert.fail("There is no cursor on this page!")
                    }
                    return String(t.text).replace(/\r/g, "").length
                }
                if (void 0 !== o.selectionStart) return r.getSelection && void 0 != typeof r.getSelection().rangeCount && 0 == r.getSelection().rangeCount && Assert.fail("There is no cursor on this page!"), o.selectionStart;
                throw new Error("Couldn't detect cursor position on this browser!")
            }, h.prototype.getExpression = function(e) {
                return e
            }, h.prototype.getXpathCount = function(e) {
                return this.browserbot.evaluateXPathCount(e, this.browserbot.getDocument())
            }, h.prototype.getCssCount = function(e) {
                return this.browserbot.evaluateCssCount(e, this.browserbot.getDocument())
            }, h.prototype.doAssignId = function(e, t) {
                this.browserbot.findElement(e).id = t
            }, h.prototype.doAllowNativeXpath = function(e) {
                "false" != e && "0" != e || (e = !1), this.browserbot.setAllowNativeXPath(e)
            }, h.prototype.doIgnoreAttributesWithoutValue = function(e) {
                "false" != e && "0" != e || (e = !1), this.browserbot.setIgnoreAttributesWithoutValue(e)
            }, h.prototype.doWaitForCondition = function(e, t) {
                return h.decorateFunctionWithTimeout(function() {
                    return this.eval(e)
                }, t)
            }, h.prototype.doWaitForCondition.dontCheckAlertsAndConfirms = !0, h.prototype.doSetTimeout = function(e) {
                e || (e = h.DEFAULT_TIMEOUT), this.defaultTimeout = e
            }, h.prototype.doWaitForPageToLoad = function(e) {
                if (null == window["proxyInjectionMode"] || !window["proxyInjectionMode"]) return this.makePageLoadCondition(e)
            }, h.prototype.doWaitForFrameToLoad = function(e, t) {
                if (null == window["proxyInjectionMode"] || !window["proxyInjectionMode"]) return this.makePageLoadCondition(t)
            }, h.prototype._isNewPageLoaded = function() {
                return this.browserbot.isNewPageLoaded()
            }, h.prototype._abortXhrRequest = function() {
                return this.browserbot.abortXhrRequest()
            }, h.prototype.doWaitForPageToLoad.dontCheckAlertsAndConfirms = !0, h.prototype.preprocessParameter = function(e) {
                if (!e.script) {
                    let t = e.match(/^javascript\{((.|\r?\n)+)\}$/);
                    if (t && t[1]) {
                        r.a.runtime.sendMessage({
                            log: {
                                type: "warn",
                                message: "parameter preprocessing using javascript{} tag is deprecated, please use execute script"
                            }
                        });
                        let e = this.eval(t[1]);
                        return null == e ? null : e.toString()
                    }
                }
                return e
            }, h.prototype.replaceKeys = function(e) {
                let t = [],
                    o = e.match(/\$\{\w+\}/g);
                if (o) {
                    let n = 0;
                    for (; n < e.length;) {
                        let r = o.shift(),
                            i = e.indexOf(r, n);
                        if (i > n && (t.push(e.substr(n, i - n)), n = i), r) {
                            if (/^\$\{KEY_\w+\}/.test(r)) {
                                let e = r.match(/\$\{KEY_(\w+)\}/)[1],
                                    o = c["a"].Keyboard.Keys[e];
                                if (!o) throw new Error(`Unrecognised key ${e}`);
                                t.push(o)
                            } else t.push(r);
                            n += r.length
                        } else n < e.length && (t.push(e.substr(n, e.length)), n = e.length)
                    }
                } else t.push(e);
                return t
            }, h.prototype.getCookie = function() {
                return this.browserbot.getDocument().cookie
            }, h.prototype.getCookieByName = function(e) {
                let t = this.browserbot.getCookieByName(e);
                if (null === t) throw new s["a"]("Cookie '" + e + "' was not found");
                return t
            }, h.prototype.isCookiePresent = function(e) {
                return !(null === this.browserbot.getCookieByName(e))
            }, h.prototype.doCreateCookie = function(e, t) {
                let o = /[^\s=\[\]\(\),"\/\?@:;]+=[^\s=\[\]\(\),"\/\?@:;]*/.test(e);
                if (!o) throw new s["a"]("Invalid parameter.");
                let n = e.trim();
                if (o = /max_age=(\d+)/.exec(t)) {
                    let e = (new Date).getTime() + 1e3 * o[1];
                    n += "; expires=" + new Date(e).toGMTString()
                }
                if (o = /path=([^\s,]+)[,]?/.exec(t)) {
                    let e = o[1];
                    u["a"].khtml && "/" != e && (e = e.replace(/\/$/, "")), n += "; path=" + e
                }
                if (o = /domain=([^\s,]+)[,]?/.exec(t)) {
                    n += "; domain=" + o[1]
                }
                this.browserbot.getDocument().cookie = n
            }, h.prototype.doDeleteCookie = function(e, t) {
                let o = "",
                    n = "",
                    r = !1,
                    i = !1,
                    s = /path=([^\s,]+)[,]?/.exec(t);
                s && (i = !0, o = s[1]), (s = /domain=([^\s,]+)[,]?/.exec(t)) && (i = !0, n = s[1]), (s = /recurse=([^\s,]+)[,]?/.exec(t)) && (i = !0, "false" == (r = s[1]) && (r = !1)), t && !i && (o = t), u["a"].khtml && "/" != o && (o = o.replace(/\/$/, "")), o = o.trim(), n = n.trim();
                let l = e.trim();
                r ? this.browserbot.recursivelyDeleteCookie(l, n, o) : this.browserbot.deleteCookie(l, n, o)
            }, h.prototype.doDeleteAllVisibleCookies = function() {
                let e = this.browserbot.getCurrentWindow(),
                    t = e.document,
                    o = this.browserbot.getAllCookieNames(t),
                    n = t.domain,
                    r = e.location.pathname;
                for (let t = 0; t < o.length; t++) this.browserbot.recursivelyDeleteCookie(o[t], n, r, e)
            }, h.prototype.doExecuteScript = function(e, t) {
                const o = this.eval(e.script, e.argv);
                if (o && "Promise" === o.constructor.name) throw new Error("Expected sync operation, instead received Promise");
                if (t) return r.a.runtime.sendMessage({
                    storeStr: o,
                    storeVar: t
                })
            }, h.prototype.doExecuteAsyncScript = function(e, t) {
                const o = this.eval(e.script, e.argv);
                if (o && "Promise" !== o.constructor.name) throw new Error(`Expected async operation, instead received ${o?o.constructor.name:o}`);
                return Promise.resolve(o).then(e => {
                    if (t) return r.a.runtime.sendMessage({
                        storeStr: e,
                        storeVar: t
                    })
                })
            }, h.prototype.doRunScript = function(e) {
                this.eval(e.script, e.argv)
            }, h.prototype.doRollup = function(e, t) {
                let o = currentTest || htmlTestRunner.currentTest,
                    n = {
                        backup: function() {
                            for (let e in this.data) this.data[e] = o[e]
                        },
                        restore: function() {
                            for (let e in this.data) o[e] = this.data[e]
                        },
                        data: {
                            requiresCallBack: null,
                            commandStarted: null,
                            nextCommand: null,
                            commandComplete: null,
                            commandError: null,
                            pendingRollupCommands: null,
                            rollupFailed: null,
                            rollupFailedMessage: null
                        }
                    },
                    r = RollupManager.getInstance().getRollupRule(e).getExpandedCommands(t);
                try {
                    n.backup(), o.requiresCallBack = !1, o.commandStarted = function() {}, o.nextCommand = function() {
                        if (0 == this.pendingRollupCommands.length) return null;
                        return this.pendingRollupCommands.shift()
                    }, o.commandComplete = function(e) {
                        e.failed && (this.rollupFailed = !0, this.rollupFailureMessages.push(e.failureMessage)), 0 == this.pendingRollupCommands.length && (e = {
                            failed: this.rollupFailed,
                            failureMessage: this.rollupFailureMessages.join("; ")
                        }, n.restore(), this.commandComplete(e))
                    }, o.commandError = function(e) {
                        n.restore(), this.commandError(e)
                    }, o.pendingRollupCommands = r, o.rollupFailed = !1, o.rollupFailureMessages = []
                } catch (e) {
                    n.restore()
                }
            }, h.prototype.doAddScript = function(e, t) {
                if (t && document.getElementById(t)) {
                    let e = "Element with id '" + t + "' already exists!";
                    throw new s["a"](e)
                }
                let o = document.getElementsByTagName("head")[0],
                    n = document.createElement("script");
                n.type = "text/javascript", t && (n.id = t), e = e.replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&amp;/g, "&"), n.text = e, o.appendChild(n)
            }, h.prototype.doRemoveScript = function(e) {
                let t = document.getElementById(e);
                t && "script" == Object(p["c"])(t) && t.parentNode.removeChild(t)
            }, h.prototype.doUseXpathLibrary = function(e) {
                this.browserbot.getXPathEngine(e) && this.browserbot.setXPathEngine(e)
            }, b.prototype.fromLocatorString = function(e) {
                let t = "label",
                    o = e,
                    n = e.match(/^([a-zA-Z]+)=(.*)/);
                if (n && (t = n[1], o = n[2]), void 0 == this.optionLocators && this.registerOptionLocators(), this.optionLocators[t]) return new this.optionLocators[t](o);
                throw new s["a"]("Unknown option locator type: " + t)
            }, b.prototype.registerOptionLocators = function() {
                this.optionLocators = {};
                for (let e in this) {
                    let t = /OptionLocatorBy([A-Z].+)$/.exec(e);
                    if (null != t) {
                        let o = Object(p["e"])(t[1]);
                        this.optionLocators[o] = this[e]
                    }
                }
            }, b.prototype.OptionLocatorByLabel = function(e) {
                this.label = e, this.labelMatcher = new d["a"](this.label), this.findOption = function(e) {
                    for (let t = 0; t < e.options.length; t++)
                        if (this.labelMatcher.matches(e.options[t].text)) return e.options[t];
                    throw new s["a"]("Option with label '" + this.label + "' not found")
                }, this.assertSelected = function(e) {
                    let t = e.options[e.selectedIndex].text;
                    Assert.matches(this.label, t)
                }
            }, b.prototype.OptionLocatorByValue = function(e) {
                this.value = e, this.valueMatcher = new d["a"](this.value), this.findOption = function(e) {
                    for (let t = 0; t < e.options.length; t++)
                        if (this.valueMatcher.matches(e.options[t].value)) return e.options[t];
                    throw new s["a"]("Option with value '" + this.value + "' not found")
                }, this.assertSelected = function(e) {
                    let t = e.options[e.selectedIndex].value;
                    Assert.matches(this.value, t)
                }
            }, b.prototype.OptionLocatorByIndex = function(e) {
                if (this.index = Number(e), isNaN(this.index) || this.index < 0) throw new s["a"]("Illegal Index: " + e);
                this.findOption = function(e) {
                    if (e.options.length <= this.index) throw new s["a"]("Index out of range.  Only " + e.options.length + " options available");
                    return e.options[this.index]
                }, this.assertSelected = function(e) {
                    Assert.equals(this.index, e.selectedIndex)
                }
            }, b.prototype.OptionLocatorById = function(e) {
                this.id = e, this.idMatcher = new d["a"](this.id), this.findOption = function(e) {
                    for (let t = 0; t < e.options.length; t++)
                        if (this.idMatcher.matches(e.options[t].id)) return e.options[t];
                    throw new s["a"]("Option with id '" + this.id + "' not found")
                }, this.assertSelected = function(e) {
                    let t = e.options[e.selectedIndex].id;
                    Assert.matches(this.id, t)
                }
            }, h.prototype.doEditContent = function(e, t) {
                let o = this.browserbot.findElement(e);
                if ("true" != o.contentEditable) throw new s["a"]("The value of contentEditable attribute of this element is not true.");
                o.innerHTML = Object(a["escapeHTML"])(t)
            }, h.prototype.doChooseCancelOnNextPrompt = function() {
                return this.browserbot.cancelNextPrompt()
            }, h.prototype.doAnswerOnNextPrompt = function(e) {
                return this.browserbot.setNextPromptResult(e)
            }, h.prototype.doAssertPrompt = function(e) {
                return this.browserbot.getPromptMessage().then(function(t) {
                    return e != t ? Promise.reject("Prompt message doesn't match actual message") : Promise.resolve(!0)
                })
            }, h.prototype.doAssertAlert = function(e) {
                return this.browserbot.getAlertMessage().then(function(t) {
                    return e != t ? Promise.reject("Alert message doesn't match actual message") : Promise.resolve(!0)
                })
            }, h.prototype.doChooseCancelOnNextConfirmation = function() {
                return this.browserbot.setNextConfirmationResult(!1)
            }, h.prototype.doChooseOkOnNextConfirmation = function() {
                return this.browserbot.setNextConfirmationResult(!0)
            }, h.prototype.doAssertConfirmation = function(e) {
                return this.browserbot.getConfirmationMessage().then(function(t) {
                    return e != t ? Promise.reject("Confirmation message doesn't match actual message") : Promise.resolve(!0)
                })
            }, h.prototype.doShowElement = function(e) {
                const t = document.createElement("link");
                t.rel = "stylesheet", t.href = r.a.runtime.getURL("/assets/highlight.css"), (document.head || document.documentElement).appendChild(t);
                const o = document.createElement("div");
                if (o.id = "selenium-highlight", document.body.appendChild(o), e.x) o.style.left = parseInt(e.x) + "px", o.style.top = parseInt(e.y) + "px", o.style.width = parseInt(e.width) + "px", o.style.height = parseInt(e.height) + "px";
                else {
                    const t = document.documentElement.getBoundingClientRect(),
                        n = this.browserbot.findElement(e).getBoundingClientRect();
                    o.style.left = parseInt(n.left - t.left) + "px", o.style.top = parseInt(n.top - t.top) + "px", o.style.width = parseInt(n.width) + "px", o.style.height = parseInt(n.height) + "px"
                }
                return o.style.position = "absolute", o.style.zIndex = "100", o.style.display = "block", o.style.pointerEvents = "none", Object(i["a"])(o, {
                    centerIfNeeded: !0
                }), o.className = "active-selenium-highlight", setTimeout(() => {
                    document.body.removeChild(o), t.parentNode.removeChild(t)
                }, 500), "element found"
            }
        },
        255: function(e, t, o) {
            "use strict";
            o(254);
            var n = o(129),
                r = o(163),
                i = o(90),
                s = o(130),
                l = o(256);
            const a = new window.global.BrowserVersion;
            t["a"] = a, window.global.browserVersion = a;
            class u {
                constructor(e) {
                    this.topWindow = e, this.topFrame = this.topWindow, this.baseUrl = window.location.href, i["a"].setWindow(window), this.count = 1, this.buttonWindow = window, this.currentWindow = this.topWindow, this.currentWindowName = null, this.allowNativeXpath = !0, this.xpathEvaluator = new XPathEvaluator("ajaxslt"), this.isSubFrameSelected = !1, this.altKeyDown = !1, this.controlKeyDown = !1, this.shiftKeyDown = !1, this.metaKeyDown = !1, this.modalDialogTest = null, this.recordedAlerts = new Array, this.recordedConfirmations = new Array, this.recordedPrompts = new Array, this.openedWindows = {}, this.openedWindows["win_ser_local"] = this.topWindow, this.nextConfirmResult = !0, this.nextPromptResult = "", this.newPageLoaded = !1, this.pageLoadError = null, this.ignoreResponseCode = !1, this.xhr = null, this.abortXhr = !1, this.isXhrSent = !1, this.isXhrDone = !1, this.xhrOpenLocation = null, this.xhrResponseCode = null, this.xhrStatusText = null, this.shouldHighlightLocatedElement = !1, this.uniqueId = "seleniumMarker" + (new Date).getTime(), this.pollingForLoad = new Object, this.permDeniedCount = new Object, this.windowPollers = new Array, this.browserbot = this;
                    let t = this;
                    Object.assign(this, c.prototype), this._registerAllLocatorFunctions(), this.recordPageLoad = function() {
                        t.newPageLoaded = !0
                    }, this.isNewPageLoaded = function() {
                        let e;
                        if (this.pageLoadError) throw e = this.pageLoadError, this.pageLoadError = null, e;
                        if (t.ignoreResponseCode) return t.newPageLoaded;
                        if (t.isXhrSent && t.isXhrDone && !(t.xhrResponseCode >= 200 && t.xhrResponseCode <= 399 || 0 == t.xhrResponseCode)) throw e = "XHR ERROR: URL = " + t.xhrOpenLocation + " Response_Code = " + t.xhrResponseCode + " Error_Message = " + t.xhrStatusText, t.abortXhr = !1, t.isXhrSent = !1, t.isXhrDone = !1, t.xhrResponseCode = null, t.xhrStatusText = null, new n["a"](e);
                        return t.newPageLoaded && (!t.isXhrSent || (t.abortXhr || t.isXhrDone))
                    }, this.setAllowNativeXPath = function(e) {
                        this.xpathEvaluator.setAllowNativeXPath(e)
                    }, this.setIgnoreAttributesWithoutValue = function(e) {
                        this.xpathEvaluator.setIgnoreAttributesWithoutValue(e)
                    }, this.setXPathEngine = function(e) {
                        this.xpathEvaluator.setCurrentEngine(e)
                    }, this.getXPathEngine = function() {
                        return this.xpathEvaluator.getCurrentEngine()
                    }
                }
            }
            t["b"] = u;
            const c = function() {};

            function d(e) {
                let t = encodeURIComponent(e).replace(".", "%2E");
                return t = t.replace("_", "%5F")
            }
            u.createForWindow = function(e, t) {
                let o;
                return (o = a.isIE ? new w(e) : a.isKonqueror ? new h(e) : a.isOpera ? new m(e) : a.isSafari ? new f(e) : new p(e)).proxyInjectionMode = t, o.getCurrentWindow(), o
            }, u.prototype.doModalDialogTest = function(e) {
                this.modalDialogTest = e
            }, u.prototype.cancelNextConfirmation = function(e) {
                this.nextConfirmResult = e
            }, u.prototype.hasAlerts = function() {
                return this.recordedAlerts.length > 0
            }, u.prototype.relayBotToRC = function(e) {
                let t = this.proxyInjectionMode;
                t || void 0 !== r["selenium"] && (t = r["selenium"].browserbot && r["selenium"].browserbot.proxyInjectionMode), t && this.relayToRC("selenium." + e)
            }, u.prototype.relayToRC = function() {
                return null
            }, u.prototype.resetPopups = function() {
                this.recordedAlerts = [], this.recordedConfirmations = [], this.recordedPrompts = []
            }, u.prototype.getNextAlert = function() {
                let e = this.recordedAlerts.shift();
                return e && (e = e.replace(/\n/g, " ")), this.relayBotToRC("browserbot.recordedAlerts"), e
            }, u.prototype.hasConfirmations = function() {
                return this.recordedConfirmations.length > 0
            }, u.prototype.getNextConfirmation = function() {
                let e = this.recordedConfirmations.shift();
                return this.relayBotToRC("browserbot.recordedConfirmations"), e
            }, u.prototype.hasPrompts = function() {
                return this.recordedPrompts.length > 0
            }, u.prototype.getNextPrompt = function() {
                let e = this.recordedPrompts.shift();
                return this.relayBotToRC("browserbot.recordedPrompts"), e
            }, u.prototype.triggerMouseEvent = function(e, t, o, n, r, s) {
                n = n || 0, r = r || 0;
                let l = 0,
                    a = 0;
                o = void 0 == typeof o || o;
                let u = i["c"].dom.getOwnerDocument(e),
                    c = i["c"].dom.getWindow(u),
                    d = u.createEvent("MouseEvents");
                d.initMouseEvent ? d.initMouseEvent(t, o, !0, c, 1, l, a, n, r, this.controlKeyDown, this.altKeyDown, this.shiftKeyDown, this.metaKeyDown, s || 0, null) : (d.initEvent(t, o, !0), d.shiftKey = this.shiftKeyDown, d.metaKey = this.metaKeyDown, d.altKey = this.altKeyDown, d.ctrlKey = this.controlKeyDown, s && (d.button = s)), e.dispatchEvent(d)
            }, u.prototype.triggerDragEvent = function(e, t) {
                const o = function(e) {
                    if (null == e) return "null";
                    if (null == e.parentElement) return "/" + e.tagName;
                    let t = e.parentElement.children,
                        n = 0,
                        r = 0,
                        i = !1;
                    for (let o = 0; o < t.length; o++) t[o].tagName != e.tagName || i ? t[o].tagName == e.tagName && r++ : (n++, r++), t[o] == e && (i = !0);
                    return r > 1 ? o(e.parentElement) + "/" + e.tagName + "[" + n + "]" : o(e.parentElement) + "/" + e.tagName
                };
                let n = "                                                      function simulateDragDrop(sourceNode, destinationNode){        function createCustomEvent(type) {                                 var event = new CustomEvent('CustomEvent');                    event.initCustomEvent(type, true, true, null);                 event.dataTransfer = {                                             data: {                                                        },                                                             setData: function(type, val) {                                     this.data[type] = val;                                     },                                                             getData: function(type) {                                          return this.data[type];                                    }                                                          };                                                             return event;                                              }                                                              function dispatchEvent(node, type, event) {                        if (node.dispatchEvent) {                                          return node.dispatchEvent(event);                          }                                                              if (node.fireEvent) {                                              return node.fireEvent('on' + type, event);                 }                                                          }                                                              var event = createCustomEvent('dragstart');                    dispatchEvent(sourceNode, 'dragstart', event);                                                                                var dropEvent = createCustomEvent('drop');                     dropEvent.dataTransfer = event.dataTransfer;                   dispatchEvent(destinationNode, 'drop', dropEvent);                                                                            var dragEndEvent = createCustomEvent('dragend');               dragEndEvent.dataTransfer = event.dataTransfer;                dispatchEvent(sourceNode, 'dragend', dragEndEvent);        }                                                              simulateDragDrop(document.evaluate('" + o(e) + "', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue, document.evaluate('" + o(t) + "', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue);    ",
                    r = this.browserbot.getCurrentWindow().document,
                    i = r.createElement("script");
                i.type = "text/javascript", i.text = n, r.body.appendChild(i)
            }, u.prototype._windowClosed = function(e) {
                try {
                    let t = e.closed;
                    return null == t || t
                } catch (e) {
                    return !0
                }
            }, u.uniqueKey = 1, u.prototype._modifyWindow = function(e) {
                if (this._windowClosed(e)) return this.proxyInjectionMode, null;
                this.proxyInjectionMode, e.seleniumKey = u.uniqueKey++;
                try {
                    this.modifyWindowToRecordPopUpDialogs(e, this)
                } catch (e) {
                    console.error(e)
                }
                if (this.proxyInjectionMode || this.modifySeparateTestWindowToDetectPageLoads(e), e.frames && e.frames.length && e.frames.length > 0)
                    for (let t = 0; t < e.frames.length; t++) try {
                        this._modifyWindow(e.frames[t])
                    } catch (e) {}
                return e
            }, u.prototype.selectWindow = function(e) {
                if (!e || "null" == e) return void this._selectTopWindow();
                let t = e.match(/^([a-zA-Z]+)=(.*)/);
                if (!t) return void this._selectWindowByWindowId(e);
                let o = t[1],
                    r = t[2];
                if ("title" == o) this._selectWindowByTitle(r);
                else if ("name" == o) this._selectWindowByName(r);
                else {
                    if ("var" != o) throw new n["a"]("Window locator not recognized: " + o); {
                        let e = this.getCurrentWindow().eval(r);
                        if (!e) throw new n["a"]("Window not found by var: " + r);
                        this._selectWindowByName(e.name)
                    }
                }
            }, u.prototype.selectPopUp = function(e) {
                e && "null" != e ? this._selectWindowByWindowId(e) : this._selectFirstNonTopWindow()
            }, u.prototype._selectTopWindow = function() {
                this.currentWindowName = null, this.currentWindow = this.topWindow, this.topFrame = this.topWindow, this.isSubFrameSelected = !1
            }, u.prototype._selectWindowByWindowId = function(e) {
                try {
                    this._selectWindowByName(e)
                } catch (t) {
                    this._selectWindowByTitle(e)
                }
            }, u.prototype._selectWindowByName = function(e) {
                this.currentWindow = this.getWindowByName(e, !1), this.topFrame = this.currentWindow, this.currentWindowName = e, this.isSubFrameSelected = !1
            }, u.prototype._selectWindowByTitle = function(e) {
                let t = this.getWindowNameByTitle(e);
                t ? this._selectWindowByName(t) : this._selectTopWindow()
            }, u.prototype._selectFirstNonTopWindow = function() {
                let e = this.getNonTopWindowNames();
                e.length && this._selectWindowByName(e[0])
            }, u.prototype.selectFrame = function(e) {
                let t;
                if (0 == e.indexOf("index=")) {
                    if (e = e.substr(6), null == (t = this.getCurrentWindow().frames[e])) throw new n["a"]("Not found: frames[" + e + "]");
                    if (!t.document) throw new n["a"]("frames[" + e + "] is not a frame");
                    this.currentWindow = t, this.isSubFrameSelected = !0
                } else if ("relative=up" == e || "relative=parent" == e) this.currentWindow = this.getCurrentWindow().parent, this.isSubFrameSelected = null != this._getFrameElement(this.currentWindow);
                else if ("relative=top" == e) this.currentWindow = this.topFrame, this.isSubFrameSelected = !1;
                else {
                    if (null == (t = this.findElement(e))) throw new n["a"]("Not found: " + e);
                    let o = !1;
                    if (t.contentWindow ? a.isHTA ? e = t.contentWindow.name : (this.currentWindow = t.contentWindow, this.isSubFrameSelected = !0, o = !0) : t.document && t.location && (this.currentWindow = t, this.isSubFrameSelected = !0, o = !0), !o) {
                        let t = this.getCurrentWindow();
                        if (t && t.frames && t.frames.length)
                            for (let n = 0; n < t.frames.length; n++)
                                if (t.frames[n].name == e) {
                                    this.currentWindow = t.frames[n], this.isSubFrameSelected = !0, o = !0;
                                    break
                                } if (!o) throw new n["a"]("Not a frame: " + e)
                    }
                }
                this.getCurrentWindow()
            }, u.prototype.doesThisFrameMatchFrameExpression = function(e, t) {
                let o, n = !1;
                0 == t.indexOf("dom=") ? (t = t.substr(4), n = !0) : 0 == t.indexOf("index=") && (t = "frames[" + t.substr(6) + "]", n = !0);
                let r = new Components.utils.Sandbox(this.currentWindow.location.href);
                r.currentFrameString = e, r.target = t;
                try {
                    o = Components.utils.evalInSandbox(e + "." + t, r)
                } catch (e) {}
                let i = this.browserbot.getCurrentWindow();
                if (null != o) try {
                    return o.window == i || o.window.uniqueId == i.uniqueId
                } catch (e) {}
                if (n) return !1;
                let s = Components.utils.evalInSandbox(e, r);
                if ("relative=up" == t) return s.window.parent == i;
                if ("relative=top" == t) return s.window.top == i;
                if (s.window == i.parent) {
                    if (i.name == t) return !0;
                    try {
                        if (this.findElement(t, s.window).contentWindow == i) return !0
                    } catch (e) {}
                }
                return !1
            }, u.prototype.abortXhrRequest = function() {
                this.ignoreResponseCode || 0 == this.abortXhr && this.isXhrSent && !this.isXhrDone && (this.abortXhr = !0, this.xhr.abort())
            }, u.prototype.onXhrStateChange = function(e) {
                if (4 == this.xhr.readyState) {
                    if (1 == this.abortXhr) return this.xhrResponseCode = 0, this.xhrStatusText = "Request Aborted", void(this.isXhrDone = !0);
                    try {
                        if ("HEAD" == e && (501 == this.xhr.status || 405 == this.xhr.status)) return this.xhr = new XMLHttpRequest, this.xhr.onreadystatechange = this.onXhrStateChange.bind(this, "GET"), this.xhr.open("GET", this.xhrOpenLocation, !0), this.xhr.setRequestHeader("Range", "bytes:0-1"), this.xhr.send(""), void(this.isXhrSent = !0);
                        this.xhrResponseCode = this.xhr.status, this.xhrStatusText = this.xhr.statusText
                    } catch (e) {
                        this.xhrResponseCode = -1, this.xhrStatusText = "Request Error"
                    }
                    this.isXhrDone = !0
                }
            }, u.prototype.openWindow = function(e, t) {
                if ("" != e && (e = "https://www.google.com"), a.isHTA) {
                    let o = this.getCurrentWindow().open(e, t, "resizable=yes");
                    r["selenium"].browserbot.openedWindows[t] = o
                } else this.getCurrentWindow().open(e, t, "resizable=yes")
            }, u.prototype.setIFrameLocation = function(e, t) {
                e.src = t
            }, u.prototype.getCurrentPage = function() {
                return this
            }, u.prototype.windowNeedsModifying = function(e, t) {
                try {
                    let t = Components.classes["@mozilla.org/xre/app-info;1"].getService(Components.interfaces.nsIXULAppInfo);
                    if (Components.classes["@mozilla.org/xpcom/version-comparator;1"].getService(Components.interfaces.nsIVersionComparator).compare(t.version, "4.0b1") >= 0) return -1 != e.alert.toString().indexOf("native code")
                } catch (e) {}
                return !e[t]
            }, u.prototype.modifyWindowToRecordPopUpDialogs = function(e, t) {
                let o = this,
                    n = i["b"].firefox.unwrap(e);
                if (n || (n = e), n.seleniumAlert = n.alert, !o.windowNeedsModifying(n, t.uniqueId)) return;
                n.alert = function(e) {
                    t.recordedAlerts.push(e), o.relayBotToRC.call(o, "browserbot.recordedAlerts")
                }, n.confirm = function(e) {
                    t.recordedConfirmations.push(e);
                    let n = t.nextConfirmResult;
                    return t.nextConfirmResult = !0, o.relayBotToRC.call(o, "browserbot.recordedConfirmations"), n
                }, n.prompt = function(e) {
                    t.recordedPrompts.push(e);
                    let n = t.nextConfirmResult ? t.nextPromptResult : null;
                    return t.nextConfirmResult = !0, t.nextPromptResult = "", o.relayBotToRC.call(o, "browserbot.recordedPrompts"), n
                };
                let s, l = n.open;
                a.isHTA && (n[s = "selenium_originalOpen" + (new Date).getTime()] = n.open);
                let u = a.isHTA,
                    c = function(e, t, n, i) {
                        let a = l;
                        u && (a = this[s]), "" != t && "_blank" != t && void 0 !== t || (t = "win_ser_" + o.count, o.count += 1);
                        let c = a(e, t, n, i);
                        return null != t && (c["seleniumWindowName"] = t), r["selenium"].browserbot.openedWindows[t] = c, c
                    };
                if (a.isHTA) {
                    s = "selenium_originalOpen" + (new Date).getTime();
                    const e = "selenium_newOpen" + (new Date).getTime();
                    let t = "this['" + s + "'] = this.open;";
                    n.eval ? (n.eval(t), n.open = c) : (t += "this.open = this['" + e + "'];", n[e] = c, n.setTimeout(t, 0))
                } else n.open = c
            }, u.prototype.modifySeparateTestWindowToDetectPageLoads = function(e) {
                if (!e) return;
                if (this._windowClosed(e)) return;
                if (this.isPollingForLoad(e)) return;
                let t = "selenium" + (new Date).getTime();
                this.pollingForLoad[t] = !0;
                let o = this._getFrameElement(e),
                    n = this._isHTASubFrame(e);
                o && !n ? (o[t] = !0, o["frame" + this.uniqueId] = t, o[this.uniqueId] = t) : (e.location[t] = !0, e[this.uniqueId] = t, this.pollForLoad(this.recordPageLoad, e, e.document, e.location, e.location.href, t))
            }, u.prototype._isHTASubFrame = function() {
                return !!a.isHTA && this.isSubFrameSelected
            }, u.prototype._getFrameElement = function(e) {
                let t, o = null;
                try {
                    o = e.frameElement
                } catch (e) {
                    t = !0
                }
                if (t) {
                    let t = !1;
                    try {
                        t = e.parent.frames[e.name]
                    } catch (e) {}
                    if (t) {
                        let o;
                        try {
                            if (o = t.frameElement) return o
                        } catch (e) {}
                        return o = this._getFrameElementByName(e.name, e.parent.document, e)
                    }
                }
                return o
            }, u.prototype._getFrameElementByName = function(e, t, o) {
                let n, r, i;
                for (n = t.getElementsByTagName("iframe"), i = 0; i < n.length; i++)
                    if ((r = n[i]).name === e) return r;
                for (n = t.getElementsByTagName("frame"), i = 0; i < n.length; i++)
                    if ((r = n[i]).name === e) return r;
                return u.prototype.locateElementByName(o.name, o.parent.document)
            }, u.prototype.pollForLoad = function(e, t, o, n, r, s) {
                try {
                    if (t = i["b"].firefox.unwrap(t), this._windowClosed(t)) return void delete this.pollingForLoad[s];
                    let l = this._isSamePage(t, o, n, r, s),
                        a = this.getReadyState(t, t.document);
                    if (!l && "complete" == a) {
                        let o = t.location.href;
                        delete this.pollingForLoad[s], this._modifyWindow(t);
                        let n, r, i = this.isPollingForLoad(t);
                        return i || this.modifySeparateTestWindowToDetectPageLoads(t), i = this.isPollingForLoad(t), r = (n = this.getCurrentWindow(!0))[this.uniqueId], void(/(TestRunner-splash|Blank)\.html\?start=true$/.test(o) || r == i && e(n))
                    }
                    this.reschedulePoller(e, t, o, n, r, s)
                } catch (e) {
                    this.pageLoadError = e
                }
            }, u.prototype._isSamePage = function(e, t, o, n, r) {
                let i = e.document,
                    s = e.location,
                    l = s.href,
                    u = this._isSameDocument(t, i),
                    c = o === s,
                    d = l.indexOf("#");
                d > 0 && (l = l.substring(0, d));
                let p = n.indexOf("#");
                p > 0 && (n = n.substring(0, p));
                let h = n === l,
                    f = s[r];
                return (a.isKonqueror || a.isSafari) && (f = !0), u && c && h && f
            }, u.prototype._isSameDocument = function(e, t) {
                return e === t
            }, u.prototype.getReadyState = function(e, t) {
                let o = t.readyState;
                if (null == o) {
                    if ((null != this.buttonWindow && null == this.buttonWindow.document.readyState || null == top.document.readyState) && void 0 !== t.getElementsByTagName && void 0 !== t.getElementById && (null != t.getElementsByTagName("body")[0] || null != t.body)) {
                        if (e.frameElement && "about:blank" == e.location.href && "about:blank" != e.frameElement.src) return null;
                        for (let t = 0; t < e.frames.length; t++)
                            if ("complete" != this.getReadyState(e.frames[t], e.frames[t].document)) return null;
                        o = "complete"
                    }
                } else "loading" == o && a.isIE && (this.pageUnloading = !0);
                return o
            }, u.prototype.XXXreschedulePoller = function(e, t, o, n, r, i) {
                let s = this;
                window.setTimeout(function() {
                    s.pollForLoad(e, t, o, n, r, i)
                }, 500)
            }, u.prototype.XXXreschedulePoller = function(e, t, o, n, r, i) {
                let s = this.buttonWindow.document,
                    l = s.createElement("button"),
                    a = s.createTextNode(i + " - " + t.name);
                l.appendChild(a);
                let u = s.getElementById("tools"),
                    c = this;
                l.onclick = function() {
                    u.removeChild(l), c.pollForLoad(e, t, o, n, r, i)
                }, u.appendChild(l), window.setTimeout(l.onclick, 500)
            }, u.prototype.reschedulePoller = function(e, t, o, n, r, i) {
                let s = this;
                this.windowPollers.push(function() {
                    s.pollForLoad(e, t, o, n, r, i)
                })
            }, u.prototype.runScheduledPollers = function() {
                let e = this.windowPollers;
                this.windowPollers = new Array;
                for (let t = 0; t < e.length; t++) e[t].call()
            }, u.prototype.isPollingForLoad = function(e) {
                let t, o = this._getFrameElement(e),
                    n = this._isHTASubFrame(e);
                return !!(t = o && !n ? o["frame" + this.uniqueId] : e[this.uniqueId]) && (!!this.pollingForLoad[t] && t)
            }, u.prototype.getWindowByName = function(e, t) {
                let o = this.openedWindows[e];
                if (o || (o = this.topWindow[e]), !o && "_blank" == e)
                    for (let e in this.openedWindows)
                        if (/^selenium_blank/.test(e)) {
                            let t;
                            o = this.openedWindows[e];
                            try {
                                this._windowClosed(o) || (t = o.location.href)
                            } catch (e) {}
                            if (t) break
                        } if (!o) throw new n["a"]("Window does not exist. If this looks like a Selenium bug, make sure to read http://seleniumhq.org/docs/02_selenium_ide.html#alerts-popups-and-multiple-windows for potential workarounds.");
                if (a.isHTA) try {
                    o.location.href
                } catch (e) {
                    o = window.open("", o.name), this.openedWindows[o.name] = o
                }
                return t || this._modifyWindow(o), o
            }, u.prototype.getWindowNameByTitle = function(e) {
                for (let t in this.openedWindows) {
                    let o = this.openedWindows[t];
                    try {
                        if (!this._windowClosed(o) && o.document.title == e) return t
                    } catch (e) {}
                }
                try {
                    if (this.topWindow.document.title == e) return ""
                } catch (e) {}
                throw new n["a"]("Could not find window with title " + e)
            }, u.prototype.getNonTopWindowNames = function() {
                let e = [];
                for (let t in this.openedWindows) {
                    let o = this.openedWindows[t];
                    this._windowClosed(o) || o == this.topWindow || e.push(t)
                }
                return e
            }, u.prototype.getCurrentWindow = function(e) {
                if (this.proxyInjectionMode) return window;
                let t = i["b"].firefox.unwrap(this.currentWindow);
                return e || (this._modifyWindow(t), this.newPageLoaded = !1), t = this._handleClosedSubFrame(t, e), i["a"].window_ = t, i["b"].firefox.unwrap(t)
            }, u.prototype.getUserWindow = function() {
                return this.getCurrentWindow(!0)
            }, u.prototype._handleClosedSubFrame = function(e, t) {
                if (this.proxyInjectionMode) return e;
                if (this.isSubFrameSelected) {
                    let o = !0;
                    if (e.parent && e.parent.frames && e.parent.frames.length)
                        for (let t = 0; t < e.parent.frames.length; t++) {
                            let n = e.parent.frames[t];
                            if (n == e || n.seleniumKey == e.seleniumKey) {
                                o = !1;
                                break
                            }
                        }
                    if (o) return this.selectFrame("relative=top"), this.getCurrentWindow(t)
                } else this._windowClosed(e) && (e = this.topWindow);
                return e
            }, u.prototype.setShouldHighlightElement = function(e) {
                this.shouldHighlightLocatedElement = e
            }, u.prototype._registerAllLocatorFunctions = function() {
                this.locationStrategies = {};
                for (let e in this) {
                    let t = /^locateElementBy([A-Z].+)$/.exec(e);
                    if (null != t) {
                        let o = this[e];
                        if ("function" != typeof o) continue;
                        let n = o.prefix || t[1].toLowerCase();
                        this.locationStrategies[n] = o
                    }
                }
                this.findElementBy = function(e, t, o, r) {
                    let i = this.locationStrategies[e];
                    if (!i) throw new n["a"]("Unrecognised locator type: '" + e + "'");
                    return i.call(this, t, o, r)
                }, this.locationStrategies["implicit"] = function(e, t, o) {
                    return e.startsWith("//") ? this.locateElementByXPath(e, t, o) : e.startsWith("document.") ? this.locateElementByDomTraversal(e, t, o) : this.locateElementByIdentifier(e, t, o)
                }
            }, u.prototype.getDocument = function() {
                return i["b"].firefox.unwrap(this.getCurrentWindow().document)
            }, u.prototype.getTitle = function() {
                let e = this.getDocument().title;
                return "string" == typeof e && (e = e.trim()), e
            }, u.prototype.getCookieByName = function(e, t) {
                t || (t = this.getDocument());
                let o = t.cookie;
                if (!o) return null;
                let n = o.split(/;/);
                for (let t = 0; t < n.length; t++) {
                    let o = n[t].trim().split(/=/);
                    if (decodeURIComponent(o[0]) === e) return decodeURIComponent(o.slice(1).join("="))
                }
                return null
            }, u.prototype.getAllCookieNames = function(e) {
                e || (e = this.getDocument());
                let t = e.cookie;
                if (!t) return [];
                let o = [],
                    n = t.split(/;/);
                for (let e = 0; e < n.length; e++) {
                    let t = n[e].trim().split(/=/),
                        r = decodeURIComponent(t[0]);
                    o.push(r)
                }
                return o
            }, u.prototype.getAllRawCookieNames = function(e) {
                e || (e = this.getDocument());
                let t = e.cookie;
                if (!t) return [];
                let o = [],
                    n = t.split(/;/);
                for (let e = 0; e < n.length; e++) {
                    let t = n[e].trim().split(/=/)[0];
                    o.push(t)
                }
                return o
            }, u.prototype.deleteCookie = function(e, t, o, n) {
                n || (n = this.getDocument());
                let r, i = (new Date).getTime() + -1 * 1e3,
                    s = this.getAllRawCookieNames(n);
                for (let t in s) {
                    if (s[t] == e) {
                        r = e;
                        break
                    }
                    if (s[t] == encodeURIComponent(e)) {
                        r = encodeURIComponent(e);
                        break
                    }
                    if (s[t] == d(e)) {
                        r = d(e);
                        break
                    }
                }
                let l = r + "=deleted; ";
                o && (l += "path=" + o + "; "), t && (l += "domain=" + t + "; "), l += "expires=" + new Date(i).toGMTString(), n.cookie = l
            }, u.prototype._maybeDeleteCookie = function(e, t, o, n) {
                return this.deleteCookie(e, t, o, n), !this.getCookieByName(e, n)
            }, u.prototype._recursivelyDeleteCookieDomains = function(e, t, o, n) {
                if (this._maybeDeleteCookie(e, t, o, n)) return !0;
                let r = t.indexOf(".");
                return 0 == r ? this._recursivelyDeleteCookieDomains(e, t.substring(1), o, n) : -1 != r ? this._recursivelyDeleteCookieDomains(e, t.substring(r), o, n) : this._maybeDeleteCookie(e, null, o, n)
            }, u.prototype._recursivelyDeleteCookie = function(e, t, o, n) {
                let r = o.lastIndexOf("/");
                if (r == o.length - 1 && r--, -1 != r) {
                    if (this._recursivelyDeleteCookie(e, t, o.substring(0, r + 1), n)) return !0
                }
                return this._recursivelyDeleteCookieDomains(e, t, o, n)
            }, u.prototype.recursivelyDeleteCookie = function(e, t, o, r) {
                r || (r = this.getCurrentWindow());
                let i = r.document;
                t || (t = i.domain), o || (o = r.location.pathname);
                let s = this._recursivelyDeleteCookie(e, "." + t, o, i);
                if (!s && !(s = this._recursivelyDeleteCookieDomains(e, "." + t, null, i))) throw new n["a"]("Couldn't delete cookie " + e)
            }, u.prototype.findElementOrNull = function(e, t) {
                e = Object(s["f"])(e), null == t && (t = this.getCurrentWindow());
                let o = i["a"].locators.findElement({
                    [e.type]: e.string
                }, t.document);
                return o = i["b"].firefox.unwrap(o)
            }, u.prototype.findElement = function(e, t) {
                let o = this.findElementOrNull(e, t);
                if (null == o) throw new n["a"]("Element " + e + " not found");
                return i["b"].firefox.unwrap(o)
            }, u.prototype.findElementsLikeWebDriver = function(e, t, o) {
                let n = {};
                n[e] = t;
                let r = i["a"].locators.findElements(n, o),
                    s = "";
                for (let e = 0; e < r.length - 1; e++) s += i["a"].inject.cache.addElement(i["b"].firefox.unwrap(r[e])) + ",";
                return r[r.length - 1] && (s += i["a"].inject.cache.addElement(i["b"].firefox.unwrap(r[r.length - 1]))), s
            }, u.prototype.locateElementByIdentifier = function(e, t, o) {
                return this.locateElementById(e, t, o) || u.prototype.locateElementByName(e, t, o) || null
            }, u.prototype.locateElementById = function(e, t) {
                let o = t.getElementById(e);
                if (o && o.getAttribute("id") === e) return o;
                if (a.isIE || a.isOpera) {
                    let n = t.getElementsByTagName("*");
                    for (let t = 0, r = n.length; t < r; ++t)
                        if ("form" == (o = n[t]).tagName.toLowerCase()) {
                            if (o.attributes["id"].nodeValue == e) return o
                        } else if (o.getAttribute("id") == e) return o;
                    return null
                }
                return null
            }, u.prototype.locateElementByName = function(e, t) {
                let o = t.getElementsByTagName("*"),
                    n = "name=" + e;
                return (o = this.selectElements(n, o, "value")).length > 0 ? o[0] : null
            }, u.prototype.locateElementByDomTraversal = function(e) {
                let t = null,
                    o = new Components.utils.Sandbox(this.currentWindow.location.href);
                o.domTraversal = e;
                try {
                    t = Components.utils.evalInSandbox(e, o)
                } catch (e) {
                    return null
                }
                return t || null
            }, u.prototype.locateElementByDomTraversal.prefix = "dom", u.prototype.locateElementByStoredReference = function(e) {
                try {
                    return i["b"].locators.findElement("stored=" + e)
                } catch (e) {
                    return null
                }
            }, u.prototype.locateElementByStoredReference.prefix = "stored", u.prototype.locateElementByWebDriver = function(e) {
                try {
                    return i["b"].locators.findElement("webdriver=" + e)
                } catch (e) {
                    return null
                }
            }, u.prototype.locateElementByWebDriver.prefix = "webdriver", u.prototype.locateElementByXPath = function(e, t) {
                return this.xpathEvaluator.selectSingleNode(t, e, null, t.createNSResolver ? t.createNSResolver(t.documentElement) : this._namespaceResolver)
            }, u.prototype.locateElementsByXPath = function(e, t) {
                return this.xpathEvaluator.selectNodes(t, e, null, t.createNSResolver ? t.createNSResolver(t.documentElement) : this._namespaceResolver)
            }, u.prototype._namespaceResolver = function(e) {
                if ("html" == e || "xhtml" == e || "x" == e) return "http://www.w3.org/1999/xhtml";
                if ("mathml" == e) return "http://www.w3.org/1998/Math/MathML";
                if ("svg" == e) return "http://www.w3.org/2000/svg";
                throw new Error("Unknown namespace: " + e + ".")
            }, u.prototype.evaluateXPathCount = function(e, t) {
                let o = Object(s["f"])(e);
                return "xpath" == o.type || "implicit" == o.type ? i["a"].locators.findElements({
                    xpath: o.string
                }, t).length : 0
            }, u.prototype.evaluateCssCount = function(e, t) {
                let o = Object(s["f"])(e);
                return "css" == o.type || "implicit" == o.type ? i["a"].locators.findElements({
                    css: o.string
                }, t).length : 0
            }, u.prototype.locateElementByLinkText = function(e, t) {
                let o = t.getElementsByTagName("a");
                for (let t = 0; t < o.length; t++) {
                    let n = o[t];
                    if (l["a"].matches(e, i["a"].dom.getVisibleText(n))) return n
                }
                return null
            }, u.prototype.locateElementByLinkText.prefix = "link", u.prototype.findAttribute = function(e) {
                let t = e.lastIndexOf("@"),
                    o = e.slice(0, t),
                    n = e.slice(t + 1),
                    r = this.findElement(o),
                    s = i["a"].dom.getAttribute(r, n);
                return i["c"].isDefAndNotNull(s) ? s.toString() : null
            }, u.prototype.selectOption = function(e, t) {
                i["a"].events.fire(e, i["a"].events.EventType.FOCUS);
                let o = !1;
                for (let n = 0; n < e.options.length; n++) {
                    let r = e.options[n];
                    r.selected && r != t ? (r.selected = !1, o = !0) : r.selected || r != t || (r.selected = !0, o = !0)
                }
                o && i["a"].events.fire(e, i["a"].events.EventType.CHANGE)
            }, u.prototype.addSelection = function(e, t) {
                this.checkMultiselect(e), i["a"].events.fire(e, i["a"].events.EventType.FOCUS), t.selected || (t.selected = !0, i["a"].events.fire(e, i["a"].events.EventType.CHANGE))
            }, u.prototype.removeSelection = function(e, t) {
                this.checkMultiselect(e), i["a"].events.fire(e, i["a"].events.EventType.FOCUS), t.selected && (t.selected = !1, i["a"].events.fire(e, i["a"].events.EventType.CHANGE))
            }, u.prototype.checkMultiselect = function(e) {
                if (!e.multiple) throw new n["a"]("Not a multi-select")
            }, u.prototype.replaceText = function(e, t) {
                i["a"].events.fire(e, i["a"].events.EventType.FOCUS), i["a"].events.fire(e, i["a"].events.EventType.SELECT);
                let o = e.getAttribute("maxLength"),
                    n = t;
                if (null != o) {
                    let e = parseInt(o);
                    t.length > e && (n = t.substr(0, e))
                }
                if ("body" == Object(s["c"])(e)) {
                    if (e.ownerDocument && e.ownerDocument.designMode) {
                        "on" == new String(e.ownerDocument.designMode).toLowerCase() && (e.innerHTML = n)
                    }
                } else e.value = n;
                try {
                    i["a"].events.fire(e, i["a"].events.EventType.CHANGE)
                } catch (e) {}
            }, u.prototype.clickElement = function(e, t, o) {
                this._fireEventOnElement("click", e, t, o)
            }, u.prototype.doubleClickElement = function(e, t, o) {
                this._fireEventOnElement("dblclick", e, t, o)
            }, u.prototype.contextMenuOnElement = function(e, t, o) {
                this._fireEventOnElement("contextmenu", e, t, o)
            }, u.prototype._modifyElementTarget = function(e) {
                let t = this.findClickableElement(e) || e;
                if (t.target)
                    if ("_blank" == t.target || /^selenium_blank/.test(t.target)) {
                        let e = Object(s["c"])(t);
                        if ("a" == e || "form" == e) {
                            const e = "win_ser_" + this.count;
                            this.count += 1, this.browserbot.openWindow("", e), t.target = e
                        }
                    } else {
                        const e = t.target;
                        this.browserbot.openWindow("", e), t.target = e
                    }
            }, u.prototype.findClickableElement = function(e) {
                if (!e.tagName) return null;
                let t = e.tagName.toLowerCase(),
                    o = e.type;
                return e.hasAttribute("onclick") || e.hasAttribute("href") || e.hasAttribute("url") || "button" == t || "input" == t && ("submit" == o || "button" == o || "image" == o || "radio" == o || "checkbox" == o || "reset" == o) ? e : null != e.parentNode ? this.findClickableElement(e.parentNode) : null
            }, u.prototype._handleClickingImagesInsideLinks = function(e, t) {
                let o = t;
                for (; null != o;) {
                    if (o.href) {
                        e.location.href = o.href;
                        break
                    }
                    o = o.parentNode
                }
            }, u.prototype._getTargetWindow = function(e) {
                let t = e.ownerDocument.defaultView;
                return e.target && (t = this._getFrameFromGlobal(e.target)), t
            }, u.prototype._getFrameFromGlobal = function(e) {
                if ("_self" == e) return this.getCurrentWindow();
                if ("_top" == e) return this.topFrame;
                if ("_parent" == e) return this.getCurrentWindow().parent;
                if ("_blank" == e) return this.getCurrentWindow().open("", "_blank");
                let t = this.findElementBy("implicit", e, this.topFrame.document, this.topFrame);
                if (t) return t.contentWindow;
                let o = this.getWindowByName(e);
                return o || this.getCurrentWindow().open("", e)
            }, u.prototype.bodyText = function() {
                if (!this.getDocument().body) throw new n["a"]("Couldn't access document.body.  Is this HTML page fully loaded?");
                return i["a"].dom.getVisibleText(this.getDocument().body)
            }, u.prototype.getAllButtons = function() {
                let e = this.getDocument().getElementsByTagName("input"),
                    t = [];
                for (let o = 0; o < e.length; o++) "button" != e[o].type && "submit" != e[o].type && "reset" != e[o].type || t.push(e[o].id);
                return t
            }, u.prototype.getAllFields = function() {
                let e = this.getDocument().getElementsByTagName("input"),
                    t = [];
                for (let o = 0; o < e.length; o++) "text" == e[o].type && t.push(e[o].id);
                return t
            }, u.prototype.getAllLinks = function() {
                let e = this.getDocument().getElementsByTagName("a"),
                    t = [];
                for (let o = 0; o < e.length; o++) t.push(e[o].id);
                return t
            }, u.prototype.goBack = function() {
                this.getCurrentWindow().history.back()
            }, u.prototype.goForward = function() {
                this.getCurrentWindow().history.forward()
            }, u.prototype.close = function() {
                if (a.isIE) try {
                    return this.topFrame.name = (new Date).getTime(), window.open("", this.topFrame.name, ""), void this.topFrame.close()
                } catch (e) {}
                a.isChrome || a.isSafari || a.isOpera ? this.topFrame.close() : this.getCurrentWindow().eval("window.top.close();")
            }, u.prototype.refresh = function() {
                this.getCurrentWindow().location.reload(!0)
            }, u.prototype.selectElementsBy = function(e, t, o) {
                let r = u.filterFunctions[e];
                if (!r) throw new n["a"]("Unrecognised element-filter type: '" + e + "'");
                return r(t, o)
            }, u.filterFunctions = {}, u.filterFunctions.name = function(e, t) {
                let o = [];
                for (let n = 0; n < t.length; n++) t[n].name === e && o.push(t[n]);
                return o
            }, u.filterFunctions.value = function(e, t) {
                let o = [];
                for (let n = 0; n < t.length; n++) t[n].value === e && o.push(t[n]);
                return o
            }, u.filterFunctions.index = function(e, t) {
                if (e = Number(e), isNaN(e) || e < 0) throw new n["a"]("Illegal Index: " + e);
                if (t.length <= e) throw new n["a"]("Index out of range: " + e);
                return [t[e]]
            }, u.prototype.selectElements = function(e, t, o) {
                let n = o || "value",
                    r = e.match(/^([A-Za-z]+)=(.+)/);
                return r && (n = r[1].toLowerCase(), e = r[2]), this.selectElementsBy(n, e, t)
            }, u.prototype.locateElementByCss = function(e, t) {
                let o = i["a"].locators.findElements({
                    css: e
                }, t);
                return 0 != o.length ? o[0] : null
            }, u.prototype.cancelNextPrompt = function() {
                return this.setNextPromptResult(null)
            }, u.prototype.setNextPromptResult = function(e) {
                this.promptResponse = !1;
                let t = this;
                return window.postMessage({
                    direction: "from-content-script",
                    command: "setNextPromptResult",
                    target: e
                }, "*"), new Promise(function(e, o) {
                    let n = 0,
                        r = setInterval(function() {
                            t.promptResponse ? (e(), t.promptResponse = !1, clearInterval(r)) : ++n > 60 && (o("No response"), clearInterval(r))
                        }, 500)
                })
            }, u.prototype.getPromptMessage = function() {
                this.promptResponse = !1, this.promptMessage = null;
                let e = this;
                return window.postMessage({
                    direction: "from-content-script",
                    command: "getPromptMessage"
                }, "*"), new Promise(function(t, o) {
                    let n = 0,
                        r = setInterval(function() {
                            e.promptResponse ? (t(e.promptMessage), e.promptResponse = !1, e.promptMessage = null, clearInterval(r)) : ++n > 60 && (o("No response"), clearInterval(r))
                        }, 500)
                })
            }, u.prototype.setNextConfirmationResult = function(e) {
                this.confirmationResponse = !1;
                let t = this;
                return window.postMessage({
                    direction: "from-content-script",
                    command: "setNextConfirmationResult",
                    target: e
                }, "*"), new Promise(function(e, o) {
                    let n = 0,
                        r = setInterval(function() {
                            t.confirmationResponse ? (e(), t.confirmationResponse = !1, clearInterval(r)) : ++n > 60 && (o("No response"), clearInterval(r))
                        }, 500)
                })
            }, u.prototype.getConfirmationMessage = function() {
                this.confirmationResponse = !1, this.confirmationMessage = null;
                let e = this;
                return window.postMessage({
                    direction: "from-content-script",
                    command: "getConfirmationMessage"
                }, "*"), new Promise(function(t, o) {
                    let n = 0,
                        r = setInterval(function() {
                            e.confirmationResponse ? (t(e.confirmationMessage), e.confirmationResponse = !1, e.confirmationMessage = null, clearInterval(r)) : ++n > 60 && (o("No response"), clearInterval(r))
                        }, 500)
                })
            }, u.prototype.getAlertMessage = function() {
                let e = this;
                return new Promise(function(t, o) {
                    let n = 0,
                        r = setInterval(function() {
                            e.alertResponse ? (t(e.alertMessage), e.alertResponse = !1, e.alertMessage = null, clearInterval(r)) : ++n > 60 && (o("No response!!!!"), clearInterval(r))
                        }, 500)
                })
            };
            class p extends u {
                constructor(e) {
                    super(e)
                }
            }
            class h extends u {
                constructor(e) {
                    super(e)
                }
            }
            h.prototype.setIFrameLocation = function(e, t) {
                e.src = "about:blank", e.src = t
            }, h.prototype._isSameDocument = function(e, t) {
                return e ? e.location == t.location : e === t
            };
            class f extends u {
                constructor(e) {
                    super(e)
                }
            }
            f.prototype.setIFrameLocation = h.prototype.setIFrameLocation;
            class m extends u {
                constructor(e) {
                    super(e)
                }
            }
            m.prototype.setIFrameLocation = function(e, t) {
                e.src == t ? e.src = t + "?reload" : e.src = t
            };
            class w extends u {
                constructor(e) {
                    super(e)
                }
            }
            w.prototype._handleClosedSubFrame = function(e, t) {
                if (this.proxyInjectionMode) return e;
                try {
                    e.location.href, this.permDenied = 0
                } catch (e) {
                    this.permDenied++
                }
                if (this._windowClosed(e) || this.permDenied > 4) {
                    if (this.isSubFrameSelected) return this.selectFrame("relative=top"), this.getCurrentWindow(t); {
                        let e = new n["a"]("Current window or frame is closed!");
                        throw e.windowClosed = !0, e
                    }
                }
                return e
            }, w.prototype.modifyWindowToRecordPopUpDialogs = function(e, t) {
                u.prototype.modifyWindowToRecordPopUpDialogs(e, t);
                let o = e.showModalDialog;
                e.showModalDialog = function(e, n, r) {
                    let i = document.location.toString(),
                        s = i.indexOf("TestRunner.html"),
                        l = i.substring(0, s),
                        a = "";
                    void 0 !== window.runOptions && (a = "&runInterval=" + runOptions.runInterval);
                    let u = l + ("TestRunner.html?auto=true&singletest=" + escape(t.modalDialogTest) + "&autoURL=" + escape(e) + a);
                    return t.modalDialogTest = null, this.proxyInjectionMode && (u = e), o(u, n, r)
                }
            }, w.prototype.modifySeparateTestWindowToDetectPageLoads = function(e) {
                this.pageUnloading = !1;
                let t = this,
                    o = function() {
                        t.pageUnloading = !0
                    };
                e.addEventListener ? e.addEventListener("beforeunload", o, !0) : e.attachEvent("onbeforeunload", o), u.prototype.modifySeparateTestWindowToDetectPageLoads.call(this, e)
            }, w.prototype.pollForLoad = function(e, t, o, n, r, i) {
                if (this.permDeniedCount[i] || (this.permDeniedCount[i] = 0), u.prototype.pollForLoad.call(this, e, t, o, n, r, i), this.pageLoadError) {
                    if (this.pageUnloading) return this.reschedulePoller(e, t, o, n, r, i), void(this.pageLoadError = null);
                    if (("Permission denied" == this.pageLoadError.message || /^Access is denied/.test(this.pageLoadError.message)) && this.permDeniedCount[i]++ < 8) {
                        if (this.permDeniedCount[i] > 4) {
                            let e, o;
                            try {
                                t.location.href, e = !0
                            } catch (e) {}
                            try {
                                this.getCurrentWindow(!0).location.href, o = !0
                            } catch (e) {}
                            if (o & !e) return void(this.pageLoadError = null)
                        }
                        return this.reschedulePoller(e, t, o, n, r, i), void(this.pageLoadError = null)
                    }
                }
            }, w.prototype._windowClosed = function(e) {
                try {
                    let t = e.closed;
                    if (!t) try {
                        e.document
                    } catch (e) {
                        return "Permission denied" != e.message && !/^Access is denied/.test(e.message)
                    }
                    return null == t || t
                } catch (e) {
                    return !!a.isHTA && "Permission denied" != e.message
                }
            }, w.prototype.locateElementByIdentifer = function(e, t) {
                return t.getElementById(e)
            }, f.prototype.modifyWindowToRecordPopUpDialogs = function(e, t) {
                u.prototype.modifyWindowToRecordPopUpDialogs(e, t);
                let o = e.open;
                e.open = function(t, n, r, i) {
                    if (t.startsWith("http://") || t.startsWith("https://") || t.startsWith("/")) return o(t, n, r, i);
                    let s = e.location.pathname || "/";
                    s = s.replace(/\/[^\/]*$/, "/"), t = t.replace(/^\.\//, "");
                    let l = o(s + t, n, r, i);
                    return null != n && (l["seleniumWindowName"] = n), l
                }
            }, p.prototype._fireEventOnElement = function(e, t, o, n) {
                let r = this.getCurrentWindow();
                i["a"].events.fire(t, i["a"].events.EventType.FOCUS), t.addEventListener(e, function() {}, !1), this.browserbot.triggerMouseEvent(t, e, !0, o, n), this._windowClosed(r)
            }, m.prototype._fireEventOnElement = function(e, t, o, n) {
                let r = this.getCurrentWindow();
                i["a"].events.fire(t, i["a"].events.EventType.FOCUS), this._modifyElementTarget(t), this.browserbot.triggerMouseEvent(t, e, !0, o, n), this._windowClosed(r)
            }, h.prototype._fireEventOnElement = function(e, t, o, n) {
                let r = this.getCurrentWindow();
                i["a"].events.fire(t, i["a"].events.EventType.FOCUS), this._modifyElementTarget(t), t[e] ? t[e]() : this.browserbot.triggerMouseEvent(t, e, !0, o, n), this._windowClosed(r)
            }, f.prototype._fireEventOnElement = function(e, t, o, n) {
                i["a"].events.fire(t, i["a"].events.EventType.FOCUS), this._modifyElementTarget(t), t[e] ? t[e]() : this.browserbot.triggerMouseEvent(t, e, !0, o, n)
            }, f.prototype.refresh = function() {
                let e = this.getCurrentWindow();
                if (e.location.hash) {
                    e.location.hash = "";
                    let t = function() {
                        e.location.reload(!0)
                    };
                    window.setTimeout(t, 1)
                } else e.location.reload(!0)
            }, w.prototype._fireEventOnElement = function(e, t, o, n) {
                let r = this.getCurrentWindow();
                i["a"].events.fire(t, i["a"].events.EventType.FOCUS);
                let s = t.checked,
                    l = !1,
                    a = function() {
                        l = !0
                    };
                r.addEventListener ? r.addEventListener("beforeunload", a, !0) : r.attachEvent("onbeforeunload", a), this._modifyElementTarget(t), t[e] ? t[e]() : this.browserbot.triggerMouseEvent(t, e, !0, o, n);
                try {
                    if (r.removeEventListener ? r.removeEventListener("onbeforeunload", a, !0) : r.detachEvent("onbeforeunload", a), this._windowClosed(r)) return;
                    void 0 != typeof t.checked && s != t.checked && i["a"].events.fire(t, i["a"].events.EventType.CHANGE)
                } catch (e) {
                    if (l) return;
                    throw e
                }
            }
        },
        256: function(e, t, o) {
            "use strict";
            t["a"] = r;
            var n = o(129);

            function r(e) {
                this.selectStrategy(e)
            }
            r.prototype = {
                selectStrategy: function(e) {
                    this.pattern = e;
                    let t = "glob";
                    if (/^([a-z-]+):(.*)/.test(e)) {
                        const o = RegExp.$1,
                            n = RegExp.$2;
                        r.strategies[o] && (t = o, e = n)
                    }
                    const o = r.strategies[t];
                    if (!o) throw new n["a"]("cannot find PatternMatcher.strategies." + t);
                    this.strategy = o, this.matcher = new o(e)
                },
                matches: function(e) {
                    return this.matcher.matches(e + "")
                }
            }, r.matches = function(e, t) {
                return new r(e).matches(t)
            }, r.strategies = {
                exact: function(e) {
                    this.expected = e, this.matches = function(e) {
                        return e == this.expected
                    }
                },
                regexp: function(e) {
                    this.regexp = new RegExp(e), this.matches = function(e) {
                        return this.regexp.test(e)
                    }
                },
                regex: function(e) {
                    this.regexp = new RegExp(e), this.matches = function(e) {
                        return this.regexp.test(e)
                    }
                },
                regexpi: function(e) {
                    this.regexp = new RegExp(e, "i"), this.matches = function(e) {
                        return this.regexp.test(e)
                    }
                },
                regexi: function(e) {
                    this.regexp = new RegExp(e, "i"), this.matches = function(e) {
                        return this.regexp.test(e)
                    }
                },
                globContains: function(e) {
                    this.regexp = new RegExp(r.regexpFromGlobContains(e)), this.matches = function(e) {
                        return this.regexp.test(e)
                    }
                },
                glob: function(e) {
                    this.regexp = new RegExp(r.regexpFromGlob(e)), this.matches = function(e) {
                        return this.regexp.test(e)
                    }
                }
            }, r.convertGlobMetaCharsToRegexpMetaChars = function(e) {
                let t = e;
                return t = (t = (t = t.replace(/([.^$+(){}\[\]\\|])/g, "\\$1")).replace(/\?/g, "(.|[\r\n])")).replace(/\*/g, "(.|[\r\n])*")
            }, r.regexpFromGlobContains = function(e) {
                return r.convertGlobMetaCharsToRegexpMetaChars(e)
            }, r.regexpFromGlob = function(e) {
                return "^" + r.convertGlobMetaCharsToRegexpMetaChars(e) + "$"
            }
        },
        355: function(e, t, o) {
            e.exports = o(163)
        },
        413: function(e, t, o) {
            "use strict";
            t["a"] = class {
                constructor(e, t) {
                    this.callback = e, this.cleanupCallback = t, this.win = window;
                    const o = this.win.document,
                        n = o.createElement("div");
                    n.setAttribute("style", "display: none;"), o.body.insertBefore(n, o.body.firstChild), this.div = n, this.e = null, this.r = null, this.banner = o.createElement("div"), this.banner.setAttribute("style", "position: fixed;top: 0;left: 0;bottom: 0;right: 0;background: trasparent;z-index: 10000;");
                    const r = o.createElement("div");
                    r.setAttribute("style", "pointer-events: none;display: flex;align-items: center;justify-content: center;flex-direction: row;position: fixed;top: 20%;left: 50%;transform: translateX(-50%);background: #f7f7f7;color: #114990;font-size: 22px;font-weight: 200;z-index: 10001;font-family: system, -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Arial, sans-serif;box-shadow: 0 7px 10px 0 rgba(0,0,0,0.1);border: 1px black solid; border-radius: 50px;padding: 10px;");
                    const i = o.createElement("img");
                    i.src = browser.runtime.getURL("/icons/icon_light128.png"), i.setAttribute("style", "width: 28px;margin: 0 10px;"), r.appendChild(i);
                    const s = o.createElement("span");
                    s.setAttribute("style", "border-left: 1px solid #c6c6c6;padding: 3px 10px;"), s.innerText = "Select an element", r.appendChild(s), setTimeout(() => {
                        this.banner.addEventListener("mousemove", () => {
                            setTimeout(() => {
                                this.banner.style.visibility = "hidden"
                            }, 300)
                        }, !1)
                    }, 300), this.banner.appendChild(r), o.body.insertBefore(this.banner, n), o.addEventListener("mousemove", this, !0), o.addEventListener("click", this, !0)
                }
                cleanup() {
                    try {
                        if (this.div && (this.div.parentNode && this.div.parentNode.removeChild(this.div), this.div = null), this.header && (this.header.parentNode && this.header.parentNode.removeChild(this.header), this.header = null), this.win) {
                            const e = this.win.document;
                            e.removeEventListener("mousemove", this, !0), e.removeEventListener("click", this, !0)
                        }
                    } catch (e) {
                        if ("TypeError: can't access dead object" != e) throw e
                    }
                    this.win = null, this.cleanupCallback && this.cleanupCallback()
                }
                handleEvent(e) {
                    switch (e.type) {
                        case "mousemove":
                            this.highlight(e.target.ownerDocument, e.clientX, e.clientY);
                            break;
                        case "click":
                            0 == e.button && this.e && this.callback && this.callback(this.e, this.win), e.preventDefault(), e.stopPropagation(), this.cleanup()
                    }
                }
                highlight(e, t, o) {
                    if (e) {
                        const n = e.elementFromPoint(t, o);
                        n && n != this.e && this.highlightElement(n)
                    }
                }
                highlightElement(e) {
                    if (!e || e == this.e || e === this.banner) return;
                    this.e = e;
                    const t = e.getBoundingClientRect(),
                        o = this.r;
                    if (t.left >= 0 && t.top >= 0 && t.width > 0 && t.height > 0) {
                        if (o && t.top == o.top && t.left == o.left && t.width == o.width && t.height == o.height) return;
                        this.r = t;
                        const e = "pointer-events: none; position: absolute; background-color: rgb(78, 171, 230); opacity: 0.4; border: 1px solid #0e0e0e; z-index: 1000000;",
                            n = `top:${t.top+this.win.scrollY}px; left:${t.left+this.win.scrollX}px; width:${t.width}px; height:${t.height}px;`;
                        this.div.setAttribute("style", e + n)
                    } else o && this.div.setAttribute("style", "display: none;")
                }
            }
        },
        416: function(e, t, o) {
            "use strict";
            t["a"] = function(e, t) {
                if (l) return t(!1);
                const o = document.createElement("div");
                o.id = "selenium-container";
                const r = document.createElement("div");
                r.id = "selenium-canvas";
                const i = function() {
                    const e = document.createElement("div");
                    e.id = "selenium-region";
                    const t = 20,
                        o = f(t);
                    o.style.top = "-5px", o.style.left = "-5px", o.style.cursor = "nw-resize", o.addEventListener("mousedown", () => {
                        const e = document.getElementById("selenium-canvas"),
                            t = document.getElementById("selenium-region");
                        e.style.cursor = "nw-resize", t.style.cursor = "nw-resize", h(Math.max(n.startX, n.endX), Math.max(n.startY, n.endY), Math.min(n.startX, n.endX), Math.min(n.startY, n.endY))
                    }), e.appendChild(o);
                    const r = f(t);
                    r.style.top = "-5px", r.style.right = "-5px", r.style.cursor = "ne-resize", r.addEventListener("mousedown", () => {
                        const e = document.getElementById("selenium-canvas"),
                            t = document.getElementById("selenium-region");
                        e.style.cursor = "ne-resize", t.style.cursor = "ne-resize", h(Math.min(n.startX, n.endX), Math.max(n.startY, n.endY), Math.max(n.startX, n.endX), Math.min(n.startY, n.endY))
                    }), e.appendChild(r);
                    const i = f(t);
                    i.style.bottom = "-5px", i.style.left = "-5px", i.style.cursor = "sw-resize", i.addEventListener("mousedown", () => {
                        const e = document.getElementById("selenium-canvas"),
                            t = document.getElementById("selenium-region");
                        e.style.cursor = "sw-resize", t.style.cursor = "sw-resize", h(Math.max(n.startX, n.endX), Math.min(n.startY, n.endY), Math.min(n.startX, n.endX), Math.max(n.startY, n.endY))
                    }), e.appendChild(i);
                    const s = f(t);
                    return s.style.bottom = "-5px", s.style.right = "-5px", s.style.cursor = "se-resize", s.addEventListener("mousedown", () => {
                        const e = document.getElementById("selenium-canvas"),
                            t = document.getElementById("selenium-region");
                        e.style.cursor = "se-resize", t.style.cursor = "se-resize", h(Math.min(n.startX, n.endX), Math.min(n.startY, n.endY), Math.max(n.startX, n.endX), Math.max(n.startY, n.endY))
                    }), e.appendChild(s), e
                }();
                (function(e, t) {
                    e.style.position = "fixed", e.style.top = 0, e.style.bottom = 0, e.style.right = 0, e.style.left = 0, e.style.zIndex = "10000", e.style.cursor = "crosshair", t.style.position = "absolute", t.style.zIndex = "10001", t.style.backgroundColor = "rgb(78, 171, 230)", t.style.opacity = "0.4", t.style.border = "1px solid #0e0e0e"
                })(r, i),
                function(e) {
                    e.addEventListener("mousedown", u), e.addEventListener("mouseup", d)
                }(o);
                const s = function(e) {
                    const t = document.createElement("div");
                    t.id = "region-control-panel";
                    const o = document.createElement("div"),
                        r = document.createElement("button");
                    r.innerText = "Confirm", r.addEventListener("click", () => {
                        m(), a();
                        const t = p(n);
                        e(`x: ${t.left}, y: ${t.top}, width: ${t.width}, height: ${t.height}`)
                    });
                    const i = document.createElement("button");
                    return i.innerText = "Cancel", i.addEventListener("click", () => {
                        m(), a(), e(!1)
                    }), t.style.visibility = "hidden", o.style.display = "flex", o.style.alignItems = "center", o.style.justifyContent = "center", w(r), w(i), o.appendChild(i), o.appendChild(r), t.appendChild(o), t
                }(t);
                s.style.zIndex = "10002", e && (i.style.left = `${e.x}px`, i.style.top = `${e.y}px`, i.style.width = `${e.width}px`, i.style.height = `${e.height}px`);
                o.appendChild(s), o.appendChild(r), o.appendChild(i), document.body.appendChild(o), l = !0
            }, t["b"] = a;
            const n = {
                    startX: 0,
                    startY: 0,
                    endX: 0,
                    endY: 0
                },
                r = {
                    x: 0,
                    y: 0
                },
                i = {
                    create: 1,
                    update: 2,
                    resize: 3
                };
            let s = i.create,
                l = !1;

            function a() {
                l && document.body.removeChild(document.getElementById("selenium-container")), l = !1
            }

            function u(e) {
                const t = document.getElementById("selenium-container"),
                    o = document.getElementById("selenium-canvas"),
                    n = document.getElementById("selenium-region");
                e.stopPropagation(), "BUTTON" !== e.target.tagName && (e.target === o ? (h(e.pageX, e.pageY, e.pageX, e.pageY), s = i.create, n.style.cursor = "crosshair") : e.target === n ? (s = i.update, r.x = e.pageX, r.y = e.pageY, n.style.cursor = "move") : e.target.parentElement === n && (s = i.resize), t.addEventListener("mousemove", c))
            }

            function c(e) {
                e.stopPropagation(), "BUTTON" !== e.target.tagName && (s === i.create || s === i.resize ? h(void 0, void 0, e.pageX, e.pageY) : s === i.update && function(e, t) {
                    let o = r.x - e,
                        i = r.y - t;
                    r.x = e, r.y = t, (n.startX - o < 5 || n.endX - o < 5) && (o = 0);
                    (n.startY - i < 5 || n.endY - i < 5) && (i = 0);
                    h(n.startX - o, n.startY - i, n.endX - o, n.endY - i)
                }(e.pageX, e.pageY))
            }

            function d(e) {
                const t = document.getElementById("selenium-container"),
                    o = document.getElementById("selenium-canvas"),
                    r = document.getElementById("selenium-region");
                e.stopPropagation(), r.style.cursor = "move", o.style.cursor = "crosshair", t.removeEventListener("mousemove", c),
                    function() {
                        const e = document.getElementById("selenium-canvas").getBoundingClientRect(),
                            t = document.getElementById("region-control-panel");
                        let o = n.startY,
                            r = n.endY,
                            i = n.startX,
                            s = n.endX,
                            l = s - i;
                        e.bottom - r <= 100 && (r = o);
                        l < 0 && (i = n.endX);
                        s = n.startX, (l = Math.abs(l)) < 225 && (l = 225);
                        t.style.position = "absolute", t.style.top = r + "px", t.style.left = i + "px", t.style.right = s + "px", t.style.width = l + "px", t.style.backgroundColor = "rgba(0, 0, 0, 0.55)", t.style.visibility = "visible"
                    }()
            }

            function p(e) {
                return t = {
                    x: e.startX,
                    y: e.startY
                }, o = {
                    x: e.endX,
                    y: e.endY
                }, {
                    left: Math.min(t.x, o.x),
                    top: Math.min(t.y, o.y),
                    width: Math.abs(t.x - o.x),
                    height: Math.abs(t.y - o.y)
                };
                var t, o
            }

            function h(e, t, o, r) {
                m();
                const i = document.getElementById("selenium-region");
                e && (n.startX = e), t && (n.startY = t), o && (n.endX = o), r && (n.endY = r);
                const s = p(n);
                i.style.left = `${s.left}px`, i.style.top = `${s.top}px`, i.style.width = `${s.width}px`, i.style.height = `${s.height}px`
            }

            function f(e) {
                const t = document.createElement("div");
                return t.style.position = "absolute", t.style.height = `${e}px`, t.style.width = `${e}px`, t
            }

            function m() {
                document.getElementById("region-control-panel").style.visibility = "hidden"
            }

            function w(e) {
                e.style.color = "#656565", e.style.backgroundColor = "#F5F5F5", e.style.padding = "10px 20px", e.style.margin = "5px 8px", e.style.borderRadius = "4px", e.style.outline = "0", e.style.textTransform = "capitalize"
            }
        },
        417: function(e, t, o) {
            "use strict";
            t["a"] = function(e) {
                (s = document.createElement("script")).src = r.a.runtime.getURL("/assets/prompt.js"), (document.head || document.documentElement).appendChild(s), window === window.top && (i = (t => (function(e, t) {
                    if (e.source && e.source.top == window && e.data && "from-page-script" == e.data.direction && e.data.response) switch (e.data.response) {
                        case "prompt":
                            t.browserbot.promptResponse = !0, e.data.value && (t.browserbot.promptMessage = e.data.value);
                            break;
                        case "confirm":
                            t.browserbot.confirmationResponse = !0, e.data.value && (t.browserbot.confirmationMessage = e.data.value);
                            break;
                        case "alert":
                            t.browserbot.alertResponse = !0, e.data.value && (t.browserbot.alertMessage = e.data.value)
                    }
                })(t, e)), window.addEventListener("message", i))
            };
            var n = o(15),
                r = o.n(n);
            let i, s
        }
    }, [355])
});
//# sourceMappingURL=playback.js.map