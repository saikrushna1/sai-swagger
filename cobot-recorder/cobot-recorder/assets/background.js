! function(e, t) {
    if ("object" == typeof exports && "object" == typeof module) module.exports = t();
    else if ("function" == typeof define && define.amd) define([], t);
    else {
        var r = t();
        for (var n in r)("object" == typeof exports ? exports : e)[n] = r[n]
    }
}("undefined" != typeof self ? self : this, function() {
    return function(e) {
        var t = {};

        function r(n) {
            if (t[n]) return t[n].exports;
            var s = t[n] = {
                i: n,
                l: !1,
                exports: {}
            };
            return e[n].call(s.exports, s, s.exports, r), s.l = !0, s.exports
        }
        return r.m = e, r.c = t, r.d = function(e, t, n) {
            r.o(e, t) || Object.defineProperty(e, t, {
                configurable: !1,
                enumerable: !0,
                get: n
            })
        }, r.n = function(e) {
            var t = e && e.__esModule ? function() {
                return e["default"]
            } : function() {
                return e
            };
            return r.d(t, "a", t), t
        }, r.o = function(e, t) {
            return Object.prototype.hasOwnProperty.call(e, t)
        }, r.p = "/assets/", r(r.s = 418)
    }({
        100: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.normalizeTestsInSuite = o, t.sanitizeProjectName = a;
            var n, s = (n = r(101)) && n.__esModule ? n : {
                default: n
            };

            function i(e, t, r) {
                return t in e ? Object.defineProperty(e, t, {
                    value: r,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[t] = r, e
            }

            function o({
                suite: e,
                tests: t
            }) {
                if (!e) return;
                let r = function(e) {
                    for (var t = 1; t < arguments.length; t++) {
                        var r = null != arguments[t] ? arguments[t] : {},
                            n = Object.keys(r);
                        "function" == typeof Object.getOwnPropertySymbols && (n = n.concat(Object.getOwnPropertySymbols(r).filter(function(e) {
                            return Object.getOwnPropertyDescriptor(r, e).enumerable
                        }))), n.forEach(function(t) {
                            i(e, t, r[t])
                        })
                    }
                    return e
                }({}, e);
                return r.tests.forEach((e, n) => {
                    r.tests[n] = t.find(t => t.id === e).name
                }), r
            }

            function a(e) {
                let t = e;
                return t.startsWith("http") ? s.default.parse(e).host : t.replace(/([^a-z0-9 ._-]+)/gi, "")
            }
            e.exports = {
                normalizeTestsInSuite: o,
                sanitizeProjectName: a
            }
        },
        101: function(e, t, r) {
            "use strict";
            var n = r(102),
                s = r(103);

            function i() {
                this.protocol = null, this.slashes = null, this.auth = null, this.host = null, this.port = null, this.hostname = null, this.hash = null, this.search = null, this.query = null, this.pathname = null, this.path = null, this.href = null
            }
            t.parse = v, t.resolve = function(e, t) {
                return v(e, !1, !0).resolve(t)
            }, t.resolveObject = function(e, t) {
                return e ? v(e, !1, !0).resolveObject(t) : t
            }, t.format = function(e) {
                s.isString(e) && (e = v(e));
                return e instanceof i ? e.format() : i.prototype.format.call(e)
            }, t.Url = i;
            var o = /^([a-z0-9.+-]+:)/i,
                a = /:[0-9]*$/,
                l = /^(\/\/?(?!\/)[^\?\s]*)(\?[^\s]*)?$/,
                c = ["{", "}", "|", "\\", "^", "`"].concat(["<", ">", '"', "`", " ", "\r", "\n", "\t"]),
                m = ["'"].concat(c),
                d = ["%", "/", "?", ";", "#"].concat(m),
                p = ["/", "?", "#"],
                u = 255,
                h = /^[+a-z0-9A-Z_-]{0,63}$/,
                g = /^([+a-z0-9A-Z_-]{0,63})(.*)$/,
                f = {
                    javascript: !0,
                    "javascript:": !0
                },
                b = {
                    javascript: !0,
                    "javascript:": !0
                },
                w = {
                    http: !0,
                    https: !0,
                    ftp: !0,
                    gopher: !0,
                    file: !0,
                    "http:": !0,
                    "https:": !0,
                    "ftp:": !0,
                    "gopher:": !0,
                    "file:": !0
                },
                A = r(104);

            function v(e, t, r) {
                if (e && s.isObject(e) && e instanceof i) return e;
                var n = new i;
                return n.parse(e, t, r), n
            }
            i.prototype.parse = function(e, t, r) {
                if (!s.isString(e)) throw new TypeError("Parameter 'url' must be a string, not " + typeof e);
                var i = e.indexOf("?"),
                    a = -1 !== i && i < e.indexOf("#") ? "?" : "#",
                    c = e.split(a);
                c[0] = c[0].replace(/\\/g, "/");
                var v = e = c.join(a);
                if (v = v.trim(), !r && 1 === e.split("#").length) {
                    var y = l.exec(v);
                    if (y) return this.path = v, this.href = v, this.pathname = y[1], y[2] ? (this.search = y[2], this.query = t ? A.parse(this.search.substr(1)) : this.search.substr(1)) : t && (this.search = "", this.query = {}), this
                }
                var x = o.exec(v);
                if (x) {
                    var T = (x = x[0]).toLowerCase();
                    this.protocol = T, v = v.substr(x.length)
                }
                if (r || x || v.match(/^\/\/[^@\/]+@[^@\/]+/)) {
                    var O = "//" === v.substr(0, 2);
                    !O || x && b[x] || (v = v.substr(2), this.slashes = !0)
                }
                if (!b[x] && (O || x && !w[x])) {
                    for (var k, C, E = -1, S = 0; S < p.length; S++) {
                        -1 !== (j = v.indexOf(p[S])) && (-1 === E || j < E) && (E = j)
                    } - 1 !== (C = -1 === E ? v.lastIndexOf("@") : v.lastIndexOf("@", E)) && (k = v.slice(0, C), v = v.slice(C + 1), this.auth = decodeURIComponent(k)), E = -1;
                    for (S = 0; S < d.length; S++) {
                        var j; - 1 !== (j = v.indexOf(d[S])) && (-1 === E || j < E) && (E = j)
                    } - 1 === E && (E = v.length), this.host = v.slice(0, E), v = v.slice(E), this.parseHost(), this.hostname = this.hostname || "";
                    var L = "[" === this.hostname[0] && "]" === this.hostname[this.hostname.length - 1];
                    if (!L)
                        for (var P = this.hostname.split(/\./), R = (S = 0, P.length); S < R; S++) {
                            var N = P[S];
                            if (N && !N.match(h)) {
                                for (var M = "", I = 0, _ = N.length; I < _; I++) N.charCodeAt(I) > 127 ? M += "x" : M += N[I];
                                if (!M.match(h)) {
                                    var D = P.slice(0, S),
                                        z = P.slice(S + 1),
                                        U = N.match(g);
                                    U && (D.push(U[1]), z.unshift(U[2])), z.length && (v = "/" + z.join(".") + v), this.hostname = D.join(".");
                                    break
                                }
                            }
                        }
                    this.hostname.length > u ? this.hostname = "" : this.hostname = this.hostname.toLowerCase(), L || (this.hostname = n.toASCII(this.hostname));
                    var $ = this.port ? ":" + this.port : "",
                        B = this.hostname || "";
                    this.host = B + $, this.href += this.host, L && (this.hostname = this.hostname.substr(1, this.hostname.length - 2), "/" !== v[0] && (v = "/" + v))
                }
                if (!f[T])
                    for (S = 0, R = m.length; S < R; S++) {
                        var F = m[S];
                        if (-1 !== v.indexOf(F)) {
                            var q = encodeURIComponent(F);
                            q === F && (q = escape(F)), v = v.split(F).join(q)
                        }
                    }
                var V = v.indexOf("#"); - 1 !== V && (this.hash = v.substr(V), v = v.slice(0, V));
                var W = v.indexOf("?");
                if (-1 !== W ? (this.search = v.substr(W), this.query = v.substr(W + 1), t && (this.query = A.parse(this.query)), v = v.slice(0, W)) : t && (this.search = "", this.query = {}), v && (this.pathname = v), w[T] && this.hostname && !this.pathname && (this.pathname = "/"), this.pathname || this.search) {
                    $ = this.pathname || "";
                    var G = this.search || "";
                    this.path = $ + G
                }
                return this.href = this.format(), this
            }, i.prototype.format = function() {
                var e = this.auth || "";
                e && (e = (e = encodeURIComponent(e)).replace(/%3A/i, ":"), e += "@");
                var t = this.protocol || "",
                    r = this.pathname || "",
                    n = this.hash || "",
                    i = !1,
                    o = "";
                this.host ? i = e + this.host : this.hostname && (i = e + (-1 === this.hostname.indexOf(":") ? this.hostname : "[" + this.hostname + "]"), this.port && (i += ":" + this.port)), this.query && s.isObject(this.query) && Object.keys(this.query).length && (o = A.stringify(this.query));
                var a = this.search || o && "?" + o || "";
                return t && ":" !== t.substr(-1) && (t += ":"), this.slashes || (!t || w[t]) && !1 !== i ? (i = "//" + (i || ""), r && "/" !== r.charAt(0) && (r = "/" + r)) : i || (i = ""), n && "#" !== n.charAt(0) && (n = "#" + n), a && "?" !== a.charAt(0) && (a = "?" + a), t + i + (r = r.replace(/[?#]/g, function(e) {
                    return encodeURIComponent(e)
                })) + (a = a.replace("#", "%23")) + n
            }, i.prototype.resolve = function(e) {
                return this.resolveObject(v(e, !1, !0)).format()
            }, i.prototype.resolveObject = function(e) {
                if (s.isString(e)) {
                    var t = new i;
                    t.parse(e, !1, !0), e = t
                }
                for (var r = new i, n = Object.keys(this), o = 0; o < n.length; o++) {
                    var a = n[o];
                    r[a] = this[a]
                }
                if (r.hash = e.hash, "" === e.href) return r.href = r.format(), r;
                if (e.slashes && !e.protocol) {
                    for (var l = Object.keys(e), c = 0; c < l.length; c++) {
                        var m = l[c];
                        "protocol" !== m && (r[m] = e[m])
                    }
                    return w[r.protocol] && r.hostname && !r.pathname && (r.path = r.pathname = "/"), r.href = r.format(), r
                }
                if (e.protocol && e.protocol !== r.protocol) {
                    if (!w[e.protocol]) {
                        for (var d = Object.keys(e), p = 0; p < d.length; p++) {
                            var u = d[p];
                            r[u] = e[u]
                        }
                        return r.href = r.format(), r
                    }
                    if (r.protocol = e.protocol, e.host || b[e.protocol]) r.pathname = e.pathname;
                    else {
                        for (var h = (e.pathname || "").split("/"); h.length && !(e.host = h.shift()););
                        e.host || (e.host = ""), e.hostname || (e.hostname = ""), "" !== h[0] && h.unshift(""), h.length < 2 && h.unshift(""), r.pathname = h.join("/")
                    }
                    if (r.search = e.search, r.query = e.query, r.host = e.host || "", r.auth = e.auth, r.hostname = e.hostname || e.host, r.port = e.port, r.pathname || r.search) {
                        var g = r.pathname || "",
                            f = r.search || "";
                        r.path = g + f
                    }
                    return r.slashes = r.slashes || e.slashes, r.href = r.format(), r
                }
                var A = r.pathname && "/" === r.pathname.charAt(0),
                    v = e.host || e.pathname && "/" === e.pathname.charAt(0),
                    y = v || A || r.host && e.pathname,
                    x = y,
                    T = r.pathname && r.pathname.split("/") || [],
                    O = (h = e.pathname && e.pathname.split("/") || [], r.protocol && !w[r.protocol]);
                if (O && (r.hostname = "", r.port = null, r.host && ("" === T[0] ? T[0] = r.host : T.unshift(r.host)), r.host = "", e.protocol && (e.hostname = null, e.port = null, e.host && ("" === h[0] ? h[0] = e.host : h.unshift(e.host)), e.host = null), y = y && ("" === h[0] || "" === T[0])), v) r.host = e.host || "" === e.host ? e.host : r.host, r.hostname = e.hostname || "" === e.hostname ? e.hostname : r.hostname, r.search = e.search, r.query = e.query, T = h;
                else if (h.length) T || (T = []), T.pop(), T = T.concat(h), r.search = e.search, r.query = e.query;
                else if (!s.isNullOrUndefined(e.search)) {
                    if (O) r.hostname = r.host = T.shift(), (j = !!(r.host && r.host.indexOf("@") > 0) && r.host.split("@")) && (r.auth = j.shift(), r.host = r.hostname = j.shift());
                    return r.search = e.search, r.query = e.query, s.isNull(r.pathname) && s.isNull(r.search) || (r.path = (r.pathname ? r.pathname : "") + (r.search ? r.search : "")), r.href = r.format(), r
                }
                if (!T.length) return r.pathname = null, r.search ? r.path = "/" + r.search : r.path = null, r.href = r.format(), r;
                for (var k = T.slice(-1)[0], C = (r.host || e.host || T.length > 1) && ("." === k || ".." === k) || "" === k, E = 0, S = T.length; S >= 0; S--) "." === (k = T[S]) ? T.splice(S, 1) : ".." === k ? (T.splice(S, 1), E++) : E && (T.splice(S, 1), E--);
                if (!y && !x)
                    for (; E--; E) T.unshift("..");
                !y || "" === T[0] || T[0] && "/" === T[0].charAt(0) || T.unshift(""), C && "/" !== T.join("/").substr(-1) && T.push("");
                var j, L = "" === T[0] || T[0] && "/" === T[0].charAt(0);
                O && (r.hostname = r.host = L ? "" : T.length ? T.shift() : "", (j = !!(r.host && r.host.indexOf("@") > 0) && r.host.split("@")) && (r.auth = j.shift(), r.host = r.hostname = j.shift()));
                return (y = y || r.host && T.length) && !L && T.unshift(""), T.length ? r.pathname = T.join("/") : (r.pathname = null, r.path = null), s.isNull(r.pathname) && s.isNull(r.search) || (r.path = (r.pathname ? r.pathname : "") + (r.search ? r.search : "")), r.auth = e.auth || r.auth, r.slashes = r.slashes || e.slashes, r.href = r.format(), r
            }, i.prototype.parseHost = function() {
                var e = this.host,
                    t = a.exec(e);
                t && (":" !== (t = t[0]) && (this.port = t.substr(1)), e = e.substr(0, e.length - t.length)), e && (this.hostname = e)
            }
        },
        102: function(e, t, r) {
            (function(e, n) {
                var s;
                ! function(i) {
                    var o = "object" == typeof t && t && !t.nodeType && t,
                        a = "object" == typeof e && e && !e.nodeType && e,
                        l = "object" == typeof n && n;
                    l.global !== l && l.window !== l && l.self !== l || (i = l);
                    var c, m, d = 2147483647,
                        p = 36,
                        u = 1,
                        h = 26,
                        g = 38,
                        f = 700,
                        b = 72,
                        w = 128,
                        A = "-",
                        v = /^xn--/,
                        y = /[^\x20-\x7E]/,
                        x = /[\x2E\u3002\uFF0E\uFF61]/g,
                        T = {
                            overflow: "Overflow: input needs wider integers to process",
                            "not-basic": "Illegal input >= 0x80 (not a basic code point)",
                            "invalid-input": "Invalid input"
                        },
                        O = p - u,
                        k = Math.floor,
                        C = String.fromCharCode;

                    function E(e) {
                        throw new RangeError(T[e])
                    }

                    function S(e, t) {
                        for (var r = e.length, n = []; r--;) n[r] = t(e[r]);
                        return n
                    }

                    function j(e, t) {
                        var r = e.split("@"),
                            n = "";
                        return r.length > 1 && (n = r[0] + "@", e = r[1]), n + S((e = e.replace(x, ".")).split("."), t).join(".")
                    }

                    function L(e) {
                        for (var t, r, n = [], s = 0, i = e.length; s < i;)(t = e.charCodeAt(s++)) >= 55296 && t <= 56319 && s < i ? 56320 == (64512 & (r = e.charCodeAt(s++))) ? n.push(((1023 & t) << 10) + (1023 & r) + 65536) : (n.push(t), s--) : n.push(t);
                        return n
                    }

                    function P(e) {
                        return S(e, function(e) {
                            var t = "";
                            return e > 65535 && (t += C((e -= 65536) >>> 10 & 1023 | 55296), e = 56320 | 1023 & e), t += C(e)
                        }).join("")
                    }

                    function R(e, t) {
                        return e + 22 + 75 * (e < 26) - ((0 != t) << 5)
                    }

                    function N(e, t, r) {
                        var n = 0;
                        for (e = r ? k(e / f) : e >> 1, e += k(e / t); e > O * h >> 1; n += p) e = k(e / O);
                        return k(n + (O + 1) * e / (e + g))
                    }

                    function M(e) {
                        var t, r, n, s, i, o, a, l, c, m, g, f = [],
                            v = e.length,
                            y = 0,
                            x = w,
                            T = b;
                        for ((r = e.lastIndexOf(A)) < 0 && (r = 0), n = 0; n < r; ++n) e.charCodeAt(n) >= 128 && E("not-basic"), f.push(e.charCodeAt(n));
                        for (s = r > 0 ? r + 1 : 0; s < v;) {
                            for (i = y, o = 1, a = p; s >= v && E("invalid-input"), ((l = (g = e.charCodeAt(s++)) - 48 < 10 ? g - 22 : g - 65 < 26 ? g - 65 : g - 97 < 26 ? g - 97 : p) >= p || l > k((d - y) / o)) && E("overflow"), y += l * o, !(l < (c = a <= T ? u : a >= T + h ? h : a - T)); a += p) o > k(d / (m = p - c)) && E("overflow"), o *= m;
                            T = N(y - i, t = f.length + 1, 0 == i), k(y / t) > d - x && E("overflow"), x += k(y / t), y %= t, f.splice(y++, 0, x)
                        }
                        return P(f)
                    }

                    function I(e) {
                        var t, r, n, s, i, o, a, l, c, m, g, f, v, y, x, T = [];
                        for (f = (e = L(e)).length, t = w, r = 0, i = b, o = 0; o < f; ++o)(g = e[o]) < 128 && T.push(C(g));
                        for (n = s = T.length, s && T.push(A); n < f;) {
                            for (a = d, o = 0; o < f; ++o)(g = e[o]) >= t && g < a && (a = g);
                            for (a - t > k((d - r) / (v = n + 1)) && E("overflow"), r += (a - t) * v, t = a, o = 0; o < f; ++o)
                                if ((g = e[o]) < t && ++r > d && E("overflow"), g == t) {
                                    for (l = r, c = p; !(l < (m = c <= i ? u : c >= i + h ? h : c - i)); c += p) x = l - m, y = p - m, T.push(C(R(m + x % y, 0))), l = k(x / y);
                                    T.push(C(R(l, 0))), i = N(r, v, n == s), r = 0, ++n
                                }++ r, ++t
                        }
                        return T.join("")
                    }
                    if (c = {
                            version: "1.4.1",
                            ucs2: {
                                decode: L,
                                encode: P
                            },
                            decode: M,
                            encode: I,
                            toASCII: function(e) {
                                return j(e, function(e) {
                                    return y.test(e) ? "xn--" + I(e) : e
                                })
                            },
                            toUnicode: function(e) {
                                return j(e, function(e) {
                                    return v.test(e) ? M(e.slice(4).toLowerCase()) : e
                                })
                            }
                        }, 1) void 0 === (s = function() {
                        return c
                    }.call(t, r, t, e)) || (e.exports = s);
                    else if (o && a)
                        if (e.exports == o) a.exports = c;
                        else
                            for (m in c) c.hasOwnProperty(m) && (o[m] = c[m]);
                    else i.punycode = c
                }(this)
            }).call(t, r(58)(e), r(8))
        },
        103: function(e, t, r) {
            "use strict";
            e.exports = {
                isString: function(e) {
                    return "string" == typeof e
                },
                isObject: function(e) {
                    return "object" == typeof e && null !== e
                },
                isNull: function(e) {
                    return null === e
                },
                isNullOrUndefined: function(e) {
                    return null == e
                }
            }
        },
        104: function(e, t, r) {
            "use strict";
            t.decode = t.parse = r(105), t.encode = t.stringify = r(106)
        },
        105: function(e, t, r) {
            "use strict";

            function n(e, t) {
                return Object.prototype.hasOwnProperty.call(e, t)
            }
            e.exports = function(e, t, r, i) {
                t = t || "&", r = r || "=";
                var o = {};
                if ("string" != typeof e || 0 === e.length) return o;
                var a = /\+/g;
                e = e.split(t);
                var l = 1e3;
                i && "number" == typeof i.maxKeys && (l = i.maxKeys);
                var c = e.length;
                l > 0 && c > l && (c = l);
                for (var m = 0; m < c; ++m) {
                    var d, p, u, h, g = e[m].replace(a, "%20"),
                        f = g.indexOf(r);
                    f >= 0 ? (d = g.substr(0, f), p = g.substr(f + 1)) : (d = g, p = ""), u = decodeURIComponent(d), h = decodeURIComponent(p), n(o, u) ? s(o[u]) ? o[u].push(h) : o[u] = [o[u], h] : o[u] = h
                }
                return o
            };
            var s = Array.isArray || function(e) {
                return "[object Array]" === Object.prototype.toString.call(e)
            }
        },
        106: function(e, t, r) {
            "use strict";
            var n = function(e) {
                switch (typeof e) {
                    case "string":
                        return e;
                    case "boolean":
                        return e ? "true" : "false";
                    case "number":
                        return isFinite(e) ? e : "";
                    default:
                        return ""
                }
            };
            e.exports = function(e, t, r, a) {
                return t = t || "&", r = r || "=", null === e && (e = void 0), "object" == typeof e ? i(o(e), function(o) {
                    var a = encodeURIComponent(n(o)) + r;
                    return s(e[o]) ? i(e[o], function(e) {
                        return a + encodeURIComponent(n(e))
                    }).join(t) : a + encodeURIComponent(n(e[o]))
                }).join(t) : a ? encodeURIComponent(n(a)) + r + encodeURIComponent(n(e)) : ""
            };
            var s = Array.isArray || function(e) {
                return "[object Array]" === Object.prototype.toString.call(e)
            };

            function i(e, t) {
                if (e.map) return e.map(t);
                for (var r = [], n = 0; n < e.length; n++) r.push(t(e[n], n));
                return r
            }
            var o = Object.keys || function(e) {
                var t = [];
                for (var r in e) Object.prototype.hasOwnProperty.call(e, r) && t.push(r);
                return t
            }
        },
        15: function(e, t, r) {
            var n, s, i;
            ! function(r, o) {
                if (1) s = [e], void 0 === (i = "function" == typeof(n = o) ? n.apply(t, s) : n) || (e.exports = i);
                else if (void 0 !== t) o(e);
                else {
                    var a = {
                        exports: {}
                    };
                    o(a), r.browser = a.exports
                }
            }(this, function(e) {
                "use strict";
                if ("undefined" == typeof browser || Object.getPrototypeOf(browser) !== Object.prototype) {
                    const t = "The message port closed before a response was received.",
                        r = "Returning a Promise is the preferred way to send a reply from an onMessage/onMessageExternal listener, as the sendResponse will be removed from the specs (See https://developer.mozilla.org/docs/Mozilla/Add-ons/WebExtensions/API/runtime/onMessage)",
                        n = () => {
                            const e = {
                                alarms: {
                                    clear: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    clearAll: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    get: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    getAll: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    }
                                },
                                bookmarks: {
                                    create: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    get: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getChildren: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getRecent: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getSubTree: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getTree: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    move: {
                                        minArgs: 2,
                                        maxArgs: 2
                                    },
                                    remove: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removeTree: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    search: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    update: {
                                        minArgs: 2,
                                        maxArgs: 2
                                    }
                                },
                                browserAction: {
                                    disable: {
                                        minArgs: 0,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    },
                                    enable: {
                                        minArgs: 0,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    },
                                    getBadgeBackgroundColor: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getBadgeText: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getPopup: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getTitle: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    openPopup: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    setBadgeBackgroundColor: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    },
                                    setBadgeText: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    },
                                    setIcon: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    setPopup: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    },
                                    setTitle: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    }
                                },
                                browsingData: {
                                    remove: {
                                        minArgs: 2,
                                        maxArgs: 2
                                    },
                                    removeCache: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removeCookies: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removeDownloads: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removeFormData: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removeHistory: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removeLocalStorage: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removePasswords: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removePluginData: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    settings: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    }
                                },
                                commands: {
                                    getAll: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    }
                                },
                                contextMenus: {
                                    remove: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removeAll: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    update: {
                                        minArgs: 2,
                                        maxArgs: 2
                                    }
                                },
                                cookies: {
                                    get: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getAll: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getAllCookieStores: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    remove: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    set: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    }
                                },
                                devtools: {
                                    inspectedWindow: {
                                        eval: {
                                            minArgs: 1,
                                            maxArgs: 2
                                        }
                                    },
                                    panels: {
                                        create: {
                                            minArgs: 3,
                                            maxArgs: 3,
                                            singleCallbackArg: !0
                                        }
                                    }
                                },
                                downloads: {
                                    cancel: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    download: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    erase: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getFileIcon: {
                                        minArgs: 1,
                                        maxArgs: 2
                                    },
                                    open: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    },
                                    pause: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removeFile: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    resume: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    search: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    show: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    }
                                },
                                extension: {
                                    isAllowedFileSchemeAccess: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    isAllowedIncognitoAccess: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    }
                                },
                                history: {
                                    addUrl: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    deleteAll: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    deleteRange: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    deleteUrl: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getVisits: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    search: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    }
                                },
                                i18n: {
                                    detectLanguage: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getAcceptLanguages: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    }
                                },
                                Test: {
                                    launchWebAuthFlow: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    }
                                },
                                idle: {
                                    queryState: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    }
                                },
                                management: {
                                    get: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getAll: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    getSelf: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    setEnabled: {
                                        minArgs: 2,
                                        maxArgs: 2
                                    },
                                    uninstallSelf: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    }
                                },
                                notifications: {
                                    clear: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    create: {
                                        minArgs: 1,
                                        maxArgs: 2
                                    },
                                    getAll: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    getPermissionLevel: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    update: {
                                        minArgs: 2,
                                        maxArgs: 2
                                    }
                                },
                                pageAction: {
                                    getPopup: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getTitle: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    hide: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    },
                                    setIcon: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    setPopup: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    },
                                    setTitle: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    },
                                    show: {
                                        minArgs: 1,
                                        maxArgs: 1,
                                        fallbackToNoCallback: !0
                                    }
                                },
                                permissions: {
                                    contains: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getAll: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    remove: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    request: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    }
                                },
                                runtime: {
                                    getBackgroundPage: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    getBrowserInfo: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    getPlatformInfo: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    openOptionsPage: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    requestUpdateCheck: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    sendMessage: {
                                        minArgs: 1,
                                        maxArgs: 3
                                    },
                                    sendNativeMessage: {
                                        minArgs: 2,
                                        maxArgs: 2
                                    },
                                    setUninstallURL: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    }
                                },
                                sessions: {
                                    getDevices: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    getRecentlyClosed: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    restore: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    }
                                },
                                storage: {
                                    local: {
                                        clear: {
                                            minArgs: 0,
                                            maxArgs: 0
                                        },
                                        get: {
                                            minArgs: 0,
                                            maxArgs: 1
                                        },
                                        getBytesInUse: {
                                            minArgs: 0,
                                            maxArgs: 1
                                        },
                                        remove: {
                                            minArgs: 1,
                                            maxArgs: 1
                                        },
                                        set: {
                                            minArgs: 1,
                                            maxArgs: 1
                                        }
                                    },
                                    managed: {
                                        get: {
                                            minArgs: 0,
                                            maxArgs: 1
                                        },
                                        getBytesInUse: {
                                            minArgs: 0,
                                            maxArgs: 1
                                        }
                                    },
                                    sync: {
                                        clear: {
                                            minArgs: 0,
                                            maxArgs: 0
                                        },
                                        get: {
                                            minArgs: 0,
                                            maxArgs: 1
                                        },
                                        getBytesInUse: {
                                            minArgs: 0,
                                            maxArgs: 1
                                        },
                                        remove: {
                                            minArgs: 1,
                                            maxArgs: 1
                                        },
                                        set: {
                                            minArgs: 1,
                                            maxArgs: 1
                                        }
                                    }
                                },
                                tabs: {
                                    captureVisibleTab: {
                                        minArgs: 0,
                                        maxArgs: 2
                                    },
                                    create: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    detectLanguage: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    discard: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    duplicate: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    executeScript: {
                                        minArgs: 1,
                                        maxArgs: 2
                                    },
                                    get: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getCurrent: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    },
                                    getZoom: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    getZoomSettings: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    highlight: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    insertCSS: {
                                        minArgs: 1,
                                        maxArgs: 2
                                    },
                                    move: {
                                        minArgs: 2,
                                        maxArgs: 2
                                    },
                                    query: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    reload: {
                                        minArgs: 0,
                                        maxArgs: 2
                                    },
                                    remove: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    removeCSS: {
                                        minArgs: 1,
                                        maxArgs: 2
                                    },
                                    sendMessage: {
                                        minArgs: 2,
                                        maxArgs: 3
                                    },
                                    setZoom: {
                                        minArgs: 1,
                                        maxArgs: 2
                                    },
                                    setZoomSettings: {
                                        minArgs: 1,
                                        maxArgs: 2
                                    },
                                    update: {
                                        minArgs: 1,
                                        maxArgs: 2
                                    }
                                },
                                topSites: {
                                    get: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    }
                                },
                                webNavigation: {
                                    getAllFrames: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    getFrame: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    }
                                },
                                webRequest: {
                                    handlerBehaviorChanged: {
                                        minArgs: 0,
                                        maxArgs: 0
                                    }
                                },
                                windows: {
                                    create: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    get: {
                                        minArgs: 1,
                                        maxArgs: 2
                                    },
                                    getAll: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    getCurrent: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    getLastFocused: {
                                        minArgs: 0,
                                        maxArgs: 1
                                    },
                                    remove: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    update: {
                                        minArgs: 2,
                                        maxArgs: 2
                                    }
                                }
                            };
                            if (0 === Object.keys(e).length) throw new Error("api-metadata.json has not been included in browser-polyfill");
                            const n = (e, t) => (...r) => {
                                    chrome.runtime.lastError ? e.reject(chrome.runtime.lastError) : t.singleCallbackArg || r.length <= 1 ? e.resolve(r[0]) : e.resolve(r)
                                },
                                s = e => 1 == e ? "argument" : "arguments",
                                i = (e, t, r) => new Proxy(t, {
                                    apply: (t, n, s) => r.call(n, e, ...s)
                                });
                            let o = Function.call.bind(Object.prototype.hasOwnProperty);
                            const a = (e, t = {}, r = {}) => {
                                    let l = Object.create(null),
                                        c = {
                                            has: (t, r) => r in e || r in l,
                                            get(c, m, d) {
                                                if (m in l) return l[m];
                                                if (!(m in e)) return;
                                                let p = e[m];
                                                if ("function" == typeof p)
                                                    if ("function" == typeof t[m]) p = i(e, e[m], t[m]);
                                                    else if (o(r, m)) {
                                                    let t = ((e, t) => (function(r, ...i) {
                                                        if (i.length < t.minArgs) throw new Error(`Expected at least ${t.minArgs} ${s(t.minArgs)} for ${e}(), got ${i.length}`);
                                                        if (i.length > t.maxArgs) throw new Error(`Expected at most ${t.maxArgs} ${s(t.maxArgs)} for ${e}(), got ${i.length}`);
                                                        return new Promise((s, o) => {
                                                            if (t.fallbackToNoCallback) try {
                                                                r[e](...i, n({
                                                                    resolve: s,
                                                                    reject: o
                                                                }, t))
                                                            } catch (n) {
                                                                console.warn(`${e} API method doesn't seem to support the callback parameter, ` + "falling back to call it without a callback: ", n), r[e](...i), t.fallbackToNoCallback = !1, t.noCallback = !0, s()
                                                            } else t.noCallback ? (r[e](...i), s()) : r[e](...i, n({
                                                                resolve: s,
                                                                reject: o
                                                            }, t))
                                                        })
                                                    }))(m, r[m]);
                                                    p = i(e, e[m], t)
                                                } else p = p.bind(e);
                                                else {
                                                    if ("object" != typeof p || null === p || !o(t, m) && !o(r, m)) return Object.defineProperty(l, m, {
                                                        configurable: !0,
                                                        enumerable: !0,
                                                        get: () => e[m],
                                                        set(t) {
                                                            e[m] = t
                                                        }
                                                    }), p;
                                                    p = a(p, t[m], r[m])
                                                }
                                                return l[m] = p, p
                                            },
                                            set: (t, r, n, s) => (r in l ? l[r] = n : e[r] = n, !0),
                                            defineProperty: (e, t, r) => Reflect.defineProperty(l, t, r),
                                            deleteProperty: (e, t) => Reflect.deleteProperty(l, t)
                                        },
                                        m = Object.create(e);
                                    return new Proxy(m, c)
                                },
                                l = e => ({
                                    addListener(t, r, ...n) {
                                        t.addListener(e.get(r), ...n)
                                    },
                                    hasListener: (t, r) => t.hasListener(e.get(r)),
                                    removeListener(t, r) {
                                        t.removeListener(e.get(r))
                                    }
                                });
                            let c = !1;
                            const m = new class extends WeakMap {
                                    constructor(e, t = void 0) {
                                        super(t), this.createItem = e
                                    }
                                    get(e) {
                                        return this.has(e) || this.set(e, this.createItem(e)), super.get(e)
                                    }
                                }(e => "function" != typeof e ? e : function(t, n, s) {
                                    let i, o, a = !1,
                                        l = new Promise(e => {
                                            i = function(t) {
                                                c || (console.warn(r, (new Error).stack), c = !0), a = !0, e(t)
                                            }
                                        });
                                    try {
                                        o = e(t, n, i)
                                    } catch (e) {
                                        o = Promise.reject(e)
                                    }
                                    const m = !0 !== o && (e => e && "object" == typeof e && "function" == typeof e.then)(o);
                                    if (!0 !== o && !m && !a) return !1;
                                    const d = e => {
                                        e.then(e => {
                                            s(e)
                                        }, e => {
                                            let t;
                                            t = e && (e instanceof Error || "string" == typeof e.message) ? e.message : "An unexpected error occurred", s({
                                                __mozWebExtensionPolyfillReject__: !0,
                                                message: t
                                            })
                                        }).catch(e => {
                                            console.error("Failed to send onMessage rejected reply", e)
                                        })
                                    };
                                    return d(m ? o : l), !0
                                }),
                                d = (e, r, n, ...i) => {
                                    if (i.length < r.minArgs) throw new Error(`Expected at least ${r.minArgs} ${s(r.minArgs)} for ${e}(), got ${i.length}`);
                                    if (i.length > r.maxArgs) throw new Error(`Expected at most ${r.maxArgs} ${s(r.maxArgs)} for ${e}(), got ${i.length}`);
                                    return new Promise((e, r) => {
                                        const s = (({
                                            reject: e,
                                            resolve: r
                                        }, n) => {
                                            chrome.runtime.lastError ? chrome.runtime.lastError.message === t ? r() : e(chrome.runtime.lastError) : n && n.__mozWebExtensionPolyfillReject__ ? e(new Error(n.message)) : r(n)
                                        }).bind(null, {
                                            resolve: e,
                                            reject: r
                                        });
                                        i.push(s), n.sendMessage(...i)
                                    })
                                },
                                p = {
                                    runtime: {
                                        onMessage: l(m),
                                        onMessageExternal: l(m),
                                        sendMessage: d.bind(null, "sendMessage", {
                                            minArgs: 1,
                                            maxArgs: 3
                                        })
                                    },
                                    tabs: {
                                        sendMessage: d.bind(null, "sendMessage", {
                                            minArgs: 2,
                                            maxArgs: 3
                                        })
                                    }
                                },
                                u = {
                                    clear: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    get: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    },
                                    set: {
                                        minArgs: 1,
                                        maxArgs: 1
                                    }
                                };
                            return e.privacy = {
                                network: {
                                    networkPredictionEnabled: u,
                                    webRTCIPHandlingPolicy: u
                                },
                                services: {
                                    passwordSavingEnabled: u
                                },
                                websites: {
                                    hyperlinkAuditingEnabled: u,
                                    referrersEnabled: u
                                }
                            }, a(chrome, p, e)
                        };
                    e.exports = n()
                } else e.exports = browser
            })
        },
        36: function(e, t, r) {
            "use strict";
            e.exports = ((e, t) => e ? e.replace(/[`'"\\\n\r\u2028\u2029\u005c]/g, e => {
                switch (e) {
                    case `"`:
                    case `'`:
                    case "`":
                        return t ? t === e ? "\\" + e : e : "\\" + e;
                    case "\n":
                        return "\\n";
                    case "\r":
                        return "\\r";
                    case "\u2028":
                        return "\\u2028";
                    case "\u2029":
                        return "\\u2029";
                    case "\\":
                        return "\\\\";
                    default:
                        return e
                }
            }) : "")
        },
        418: function(e, t, r) {
            e.exports = r(419)
        },
        419: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            });
            var n = r(15),
                s = r.n(n),
                i = r(45);

            function o(e, t, r) {
                return t in e ? Object.defineProperty(e, t, {
                    value: r,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[t] = r, e
            }
            let a, l = void 0,
                c = {},
                m = !0;

            function d(e) {
                let t = e.windowId;
                if (l) s.a.windows.update(l, {
                    focused: !0
                }).catch(function() {
                    d(e)
                });
                else if (m) return m = !1, setTimeout(function() {
                        m = !0
                    }, 1500),
                    function() {
                        let e = {
                            height: 690,
                            width: 550
                        };
                        return s.a.storage.local.get().then(n => ((r = n.size) && p(r.height) && p(r.width) && (e.height = n.size.height, e.width = n.size.width), (t = n.origin) && u(t.top) && u(t.left) && (e.top = n.origin.top, e.left = n.origin.left), s.a.windows.create(Object.assign({
                            url: s.a.extension.getURL("index.html"),
                            type: "popup"
                        }, e)))).catch(t => (console.error(t), s.a.windows.create(Object.assign({
                            url: s.a.extension.getURL("index.html"),
                            type: "popup"
                        }, e))));
                        var t;
                        var r
                    }().then(function(e) {
                        return new Promise(function(r, n) {
                            let i = 0,
                                o = setInterval(function() {
                                    i > 100 && (n("SideeX editor has no response"), clearInterval(o)), s.a.tabs.query({
                                        active: !0,
                                        windowId: e.id,
                                        status: "complete"
                                    }).then(function(n) {
                                        1 == n.length ? (c[t] = e.id, r(e), clearInterval(o)) : i++
                                    })
                                }, 200)
                        })
                    }).then(function(e) {
                        return l = e.id, s.a.tabs.sendMessage(e.tabs[0].id, {
                            selfWindowId: e.id,
                            commWindowId: t
                        })
                    }).catch(function(e) {
                        console.log(e)
                    })
            }

            function p(e) {
                return e && "Number" === e.constructor.name && e > 50
            }

            function u(e) {
                return e >= 0 && "Number" === e.constructor.name
            }
            window.master = c, window.openedWindowIds = [], i["b"] && d({
                windowId: 0
            }), s.a.browserAction.onClicked.addListener(d), s.a.windows.onRemoved.addListener(function(e) {
                let t = Object.keys(c);
                for (let r of t) c[r] === e && (delete c[r], 1 === t.length && s.a.contextMenus.removeAll());
                e === l && (l = void 0, Promise.all(window.openedWindowIds.map(e => s.a.windows.remove(e).catch(() => {}))).then(() => {
                    window.openedWindowIds = []
                }))
            }), s.a.contextMenus.onClicked.addListener(function(e) {
                a.postMessage({
                    cmd: e.menuItemId
                })
            }), s.a.runtime.onConnect.addListener(function(e) {
                a = e
            }), s.a.runtime.onMessage.addListener(function(e) {
                e.restart && e.controller && e.controller.id && (l = void 0, s.a.runtime.sendMessage({
                    uri: "/private/close",
                    verb: "post",
                    payload: null
                }).then(() => {
                    d({
                        windowId: 0
                    }).then(() => {
                        var t = function(e) {
                            for (var t = 1; t < arguments.length; t++) {
                                var r = null != arguments[t] ? arguments[t] : {},
                                    n = Object.keys(r);
                                "function" == typeof Object.getOwnPropertySymbols && (n = n.concat(Object.getOwnPropertySymbols(r).filter(function(e) {
                                    return Object.getOwnPropertyDescriptor(r, e).enumerable
                                }))), n.forEach(function(t) {
                                    o(e, t, r[t])
                                })
                            }
                            return e
                        }({}, e);
                        delete t.restart;
                        const r = {
                            uri: "/private/connect",
                            verb: "post",
                            payload: t
                        };
                        s.a.runtime.sendMessage(r).then(s.a.runtime.sendMessage(e.controller.id, {
                            connected: !0
                        })).catch(() => {
                            s.a.runtime.sendMessage(e.controller.id, "Error Connecting to Selenium IDE")
                        })
                    })
                }))
            }), s.a.runtime.onMessageExternal.addListener((e, t, r) => {
                e.payload || (e.payload = {});
                let n = e.payload;
                return n.sender = t.id, e.uri.startsWith("/private/") ? r(!1) : (s.a.runtime.sendMessage(e).then(r).catch(() => "/control" == e.uri && "post" == e.verb ? d({
                    windowId: 0
                }).then(() => {
                    const e = {
                        uri: "/private/connect",
                        verb: "post",
                        payload: {
                            controller: {
                                id: n.sender,
                                name: n.name,
                                version: n.version,
                                commands: n.commands,
                                dependencies: n.dependencies,
                                jest: n.jest,
                                exports: n.exports
                            }
                        }
                    };
                    s.a.runtime.sendMessage(e).then(r)
                }) : e.openSeleniumIDEIfClosed ? d({
                    windowId: 0
                }).then(() => {
                    r(!0)
                }) : r({
                    error: "Selenium IDE is not active"
                })), !0)
            }), s.a.runtime.onInstalled.addListener(() => {
                1 && s.a.storage.local.set({
                    updated: !0
                })
            })
        },
        45: function(e, t, r) {
            "use strict";
            var n = r(7);
            r.n(n);
            const s = n["environment"].isProduction;
            t["a"] = s;
            const i = n["environment"].isStaging;
            t["b"] = i;
            const o = n["environment"].isTest;
            t["c"] = o;
            const a = n["userAgent"].userAgent;
            t["d"] = a
        },
        52: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.preprocessParameter = function(e, t, r, {
                ignoreEscaping: n
            }) {
                const o = function(e, {
                    preprocessor: t,
                    ignoreEscaping: r
                }) {
                    return r ? e : t && "scriptPreprocessor" === t.name ? e.replace(/"/g, "'") : (0, s.default)(e)
                }(e, {
                    preprocessor: t,
                    ignoreEscaping: n
                });
                return t ? t(o, r) : i(o, r)
            }, t.defaultPreprocessor = i, t.scriptPreprocessor = function(e) {
                let t, r = e.replace(/^\s+/, "").replace(/\s+$/, ""),
                    n = [];
                const s = {},
                    i = [];
                let o = 0;
                if (/\$\{/.exec(r)) {
                    const e = /\$\{(.*?)\}/g;
                    let a = 0;
                    for (; t = e.exec(r);) {
                        const l = t[1];
                        t.index - a > 0 && n.push(r.substring(a, t.index)), s.hasOwnProperty(l) || (s[l] = o, i.push(l), o++), n.push(`arguments[${s[l]}]`), a = e.lastIndex
                    }
                    return a < r.length && n.push(r.substring(a, r.length)), {
                        script: n.join(""),
                        argv: i
                    }
                }
                return {
                    script: r,
                    argv: i
                }
            }, t.keysPreprocessor = function(e, t) {
                let r = [],
                    n = e.match(/\$\{\w+\}/g);
                if (n) {
                    let s = 0;
                    for (; s < e.length;) {
                        let o = n.shift(),
                            a = e.indexOf(o, s);
                        if (a > s && (r.push(e.substr(s, a - s)), s = a), o) {
                            if (/^\$\{KEY_\w+\}/.test(o)) {
                                let e = o.match(/\$\{KEY_(\w+)\}/)[1];
                                r.push(`Key['${e}']`)
                            } else r.push(i(o, t));
                            s += o.length
                        } else s < e.length && (r.push(e.substr(s, e.length)), s = e.length)
                    }
                } else r.push(e);
                return r
            };
            var n, s = (n = r(36)) && n.__esModule ? n : {
                default: n
            };

            function i(e, t) {
                if (!e) return;
                const r = e.match(/\$\{(\w+)\}/);
                return r ? e.replace(r[0], t(r[1])) : e
            }
        },
        53: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.default = i, t.renderCommands = o;
            var n, s = (n = r(54)) && n.__esModule ? n : {
                default: n
            };

            function i(e, t, {
                startingLevel: r,
                newLineCount: n,
                fullPayload: i,
                originTracing: a
            } = {}) {
                if (r || (r = 0), n || (n = 1), i || (i = !1), Array.isArray(t)) return o(t, {
                    startingLevel: r,
                    commandPrefixPadding: e,
                    originTracing: a
                }); {
                    const o = (0, s.default)(t, {
                        commandPrefixPadding: e,
                        startingLevel: r
                    });
                    return i ? o : o.body && o.body.length ? o.body + "\n".repeat(n) : ""
                }
            }

            function o(e, {
                startingLevel: t,
                commandPrefixPadding: r,
                originTracing: n
            } = {}) {
                let s = "",
                    o = t;
                const a = n ? n.splice(0, 2) : void 0;
                return a && (s += i(r, a.join("\n"), {
                    startingLevel: o
                })), e.forEach((e, t) => {
                    if (n) {
                        const e = i(r, n[t], {
                            startingLevel: o
                        });
                        s += e
                    }
                    if (e) {
                        const t = i(r, e, {
                            startingLevel: o,
                            fullPayload: !0
                        });
                        o = t.endingLevel, t.skipEmitting || (s += t.body, s += "\n")
                    }
                }), s
            }
        },
        54: function(e, t, r) {
            "use strict";

            function n({
                startingLevel: e,
                commandBlock: t
            } = {}) {
                let r = 0;
                return t.commands && t.commands.length > 0 && (r = t.commands[t.commands.length - 1].level || 0), e + r + (t.endingLevelAdjustment || 0)
            }
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.default = function(e, {
                startingLevel: t,
                commandPrefixPadding: r
            } = {}) {
                if (void 0 === e) return {
                    body: void 0
                };
                t || (t = 0);
                e.startingLevelAdjustment && (t += e.startingLevelAdjustment);
                t < 0 && (t = 0);
                return "object" == typeof e.commands ? e.skipEmitting ? {
                    endingLevel: n({
                        startingLevel: t,
                        commandBlock: e
                    }),
                    skipEmitting: e.skipEmitting
                } : {
                    body: e.commands.map(e => r.repeat(t + e.level) + e.statement).join("\n"),
                    endingLevel: n({
                        startingLevel: t,
                        commandBlock: e
                    })
                } : {
                    body: e.split("\n").join("\n" + r.repeat(t)).replace(/^/, r.repeat(t)),
                    endingLevel: t
                }
            }
        },
        55: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.registerCommandEmitter = function({
                command: e,
                emitter: t,
                emitters: r
            } = {}) {
                if (r[e]) throw new Error("Unable to overwrite an existing command emitter");
                r[e] = t
            }, t.registerPreprocessors = function(e) {
                Object.keys(e).forEach(t => {
                    switch (t) {
                        case "sendKeys":
                            e[t].valuePreprocessor = n.keysPreprocessor;
                            break;
                        case "runScript":
                        case "executeScript":
                        case "executeAsyncScript":
                        case "if":
                        case "elseIf":
                        case "repeatIf":
                        case "while":
                            e[t].targetPreprocessor = n.scriptPreprocessor
                    }
                })
            }, t.registerMethod = async function(e, t, {
                generateMethodDeclaration: r,
                hooks: n
            }) {
                let s = r(e);
                s = "object" == typeof s ? s.body : s, await n.declareMethods.isRegistered(s) || t.forEach(e => {
                    n.declareMethods.register(() => e)
                })
            };
            var n = r(52)
        },
        56: function(e, t, r) {
            "use strict";

            function n(e, t, r) {
                const s = r || [];
                for (const r of e.commands)
                    if ("run" === r.command) {
                        const e = t.find(e => e.name === r.target);
                        return s.push({
                            name: e.name,
                            commands: e.commands
                        }), n(e, t, s)
                    } return s
            }
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.findReusedTestMethods = n, t.findCommandThatOpensWindow = function e(t, r) {
                let s;
                for (const i of t.commands) {
                    if (i.opensWindow) {
                        s = i;
                        break
                    }
                    if ("run" === i.command) {
                        const s = n(t, r);
                        for (const t in s) return e(s[t], s)
                    }
                }
                return s
            }
        },
        57: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.ArgTypes = void 0;
            const n = {
                alertText: {
                    name: "alert text",
                    description: "text to check"
                },
                answer: {
                    name: "answer",
                    description: "The answer to give in response to the prompt pop-up."
                },
                attributeLocator: {
                    name: "attribute locator",
                    description: `An element locator followed by an @ sign and then the name of \n    the attribute, e.g. "foo@bar".`
                },
                arrayVariableName: {
                    name: "array variable name",
                    description: "The name of a variable containing a JavaScript array."
                },
                conditionalExpression: {
                    name: "conditional expression",
                    description: `JavaScript expression that returns a boolean result for use \n    in control flow commands.`
                },
                coord: {
                    name: "coord string",
                    description: `Specifies the x,y position (e.g., - 10,20) of the mouse event \n    relative to the element found from a locator.`
                },
                expectedValue: {
                    name: "expected value",
                    description: `The result you expect a variable to contain (e.g., true, false, \n    or some other value).`
                },
                expression: {
                    name: "expression",
                    description: "The value you'd like to store."
                },
                formLocator: {
                    name: "form locator",
                    description: `An element locator for the form you want to submit.`
                },
                handle: {
                    name: "window handle",
                    description: `A handle representing a specific page (tab, or window).`
                },
                iteratorVariableName: {
                    name: "iterator variable name",
                    description: `The name of the variable used when iterating over a collection in a looping control flow command (e.g., for each).`
                },
                json: {
                    name: "json",
                    description: `A string representation of a JavaScript object.`
                },
                keySequence: {
                    name: "key sequence",
                    description: "A sequence of keys to type, can be used to send key strokes (e.g. ${KEY_ENTER})."
                },
                locator: {
                    name: "locator",
                    description: "An element locator."
                },
                locatorOfDragDestinationObject: {
                    name: "locator of drag destination object",
                    description: `The locator of an element whose location (e.g., the center-most \n    pixel within it) will be the point where locator of object to be dragged is \n    dropped.`
                },
                locatorOfObjectToBeDragged: {
                    name: "locator of object to be dragged",
                    description: "The locator of element to be dragged."
                },
                loopLimit: {
                    name: "loop limit",
                    description: `An optional argument that specifies the maximum number of times a looping control flow command can execute. This protects against infinite loops. The defaults value is set to 1000.`,
                    isOptional: !0
                },
                message: {
                    name: "message",
                    description: "The message to print."
                },
                optionLocator: {
                    name: "option",
                    description: 'An option locator, typically just an option label (e.g. "John Smith").'
                },
                optionalFlag: {
                    name: "optional flag",
                    description: `Specify a configuration option to alter the command's behavior (e.g., --disable-assertions for the run command).`,
                    isOptional: !0
                },
                pattern: {
                    name: "text",
                    description: `An exact string match. Support for pattern matching is in the \n    works. See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.`
                },
                region: {
                    name: "region",
                    description: `Specify a rectangle with coordinates and lengths (e.g., "x: 257, \n    y: 300, width: 462, height: 280").`
                },
                resolution: {
                    name: "resolution",
                    description: `Specify a window resolution using WidthxHeight. (e.g., 1280x800).`
                },
                script: {
                    name: "script",
                    description: "The JavaScript snippet to run."
                },
                selectLocator: {
                    name: "select locator",
                    description: "An element locator identifying a drop-down menu."
                },
                testCase: {
                    name: "test case",
                    description: "Test case name from the project."
                },
                text: {
                    name: "text",
                    description: "The text to use."
                },
                times: {
                    name: "times",
                    description: `The number of attempts a times control flow loop will execute \n    the commands within its block.`
                },
                url: {
                    name: "url",
                    description: "The URL to open (may be relative or absolute)."
                },
                value: {
                    name: "value",
                    description: "The value to input."
                },
                variableName: {
                    name: "variable name",
                    description: `The name of a variable without brackets.`
                },
                waitTime: {
                    name: "wait time",
                    description: "The amount of time to wait (in milliseconds)."
                },
                xpath: {
                    name: "xpath",
                    description: "The xpath expression to evaluate."
                }
            };
            t.ArgTypes = n
        },
        58: function(e, t) {
            e.exports = function(e) {
                return e.webpackPolyfill || (e.deprecate = function() {}, e.paths = [], e.children || (e.children = []), Object.defineProperty(e, "loaded", {
                    enumerable: !0,
                    get: function() {
                        return e.l
                    }
                }), Object.defineProperty(e, "id", {
                    enumerable: !0,
                    get: function() {
                        return e.i
                    }
                }), e.webpackPolyfill = 1), e
            }
        },
        7: function(e, t, r) {
            "use strict";
            var n = l(r(92)),
                s = l(r(36)),
                i = l(r(97)),
                o = l(r(99)),
                a = l(r(100));

            function l(e) {
                return e && e.__esModule ? e : {
                    default: e
                }
            }
            e.exports = {
                codeExport: n.default,
                stringEscape: s.default,
                userAgent: i.default,
                environment: o.default,
                project: a.default
            }
        },
        75: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.registerCommand = t.ArgTypes = t.TargetTypes = t.Commands = void 0;
            var n = r(94),
                s = r(57);
            const i = n.Commands;
            t.Commands = i;
            const o = n.TargetTypes;
            t.TargetTypes = o;
            const a = s.ArgTypes;
            t.ArgTypes = a;
            const l = n.registerCommand;
            t.registerCommand = l
        },
        8: function(e, t) {
            var r;
            r = function() {
                return this
            }();
            try {
                r = r || Function("return this")() || (0, eval)("this")
            } catch (e) {
                "object" == typeof window && (r = window)
            }
            e.exports = r
        },
        92: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.default = void 0;
            var n = m(r(93)),
                s = m(r(54)),
                i = r(55),
                o = m(r(95)),
                a = m(r(56)),
                l = m(r(53)),
                c = m(r(96));

            function m(e) {
                return e && e.__esModule ? e : {
                    default: e
                }
            }

            function d(e) {
                for (var t = 1; t < arguments.length; t++) {
                    var r = null != arguments[t] ? arguments[t] : {},
                        n = Object.keys(r);
                    "function" == typeof Object.getOwnPropertySymbols && (n = n.concat(Object.getOwnPropertySymbols(r).filter(function(e) {
                        return Object.getOwnPropertyDescriptor(r, e).enumerable
                    }))), n.forEach(function(t) {
                        p(e, t, r[t])
                    })
                }
                return e
            }

            function p(e, t, r) {
                return t in e ? Object.defineProperty(e, t, {
                    value: r,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[t] = r, e
            }
            var u = {
                emit: d({}, n.default),
                prettify: s.default,
                register: {
                    preprocessors: i.registerPreprocessors,
                    emitter: i.registerCommandEmitter
                },
                hook: o.default,
                find: d({}, a.default),
                render: l.default,
                parsers: c.default
            };
            t.default = u
        },
        93: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.emitCommand = p, t.emitLocation = u, t.emitSelection = h, t.emitOriginTracing = b, t.default = void 0;
            var n = c(r(36)),
                s = r(52),
                i = c(r(53)),
                o = r(55),
                a = r(56),
                l = r(75);

            function c(e) {
                return e && e.__esModule ? e : {
                    default: e
                }
            }

            function m(e) {
                for (var t = 1; t < arguments.length; t++) {
                    var r = null != arguments[t] ? arguments[t] : {},
                        n = Object.keys(r);
                    "function" == typeof Object.getOwnPropertySymbols && (n = n.concat(Object.getOwnPropertySymbols(r).filter(function(e) {
                        return Object.getOwnPropertyDescriptor(r, e).enumerable
                    }))), n.forEach(function(t) {
                        d(e, t, r[t])
                    })
                }
                return e
            }

            function d(e, t, r) {
                return t in e ? Object.defineProperty(e, t, {
                    value: r,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[t] = r, e
            }

            function p(e, t, {
                variableLookup: r,
                emitNewWindowHandling: n
            } = {}) {
                if (function(e) {
                        const t = e.command;
                        if (!t.startsWith("//")) {
                            let r = l.Commands.find(e => e[0] === t);
                            if (!r) throw new Error(`Invalid command '${t}'`);
                            if (!!(r = r[1]).target != !!e.target && !r.target.isOptional) throw new Error(`Incomplete command '${e.command}'. Missing expected target argument.`);
                            if (!!r.value != !!e.value && !r.value.isOptional) throw new Error(`Incomplete command '${t}'. Missing expected value argument.`)
                        }
                    }(e), t) {
                    const i = "storeJson" === e.command;
                    let o = t((0, s.preprocessParameter)(e.target, t.targetPreprocessor, r, {
                        ignoreEscaping: i
                    }), (0, s.preprocessParameter)(e.value, t.valuePreprocessor, r, {
                        ignoreEscaping: i
                    }));
                    return e.opensWindow && (o = n(e, o)), o
                }
            }

            function u(e, t) {
                if (/^\/\//.test(e)) return t.xpath(e);
                const r = e.split("="),
                    n = r.shift(),
                    s = r.join("=");
                if (t[n]) return t[n](s);
                throw new Error(n ? `Unknown locator ${n}` : "Locator can't be empty")
            }

            function h(e, t) {
                if (!e) throw new Error(`Location can't be empty`);
                const [r, n] = e.split("=");
                if (t[r] && n) {
                    return t[r](n)
                }
                if (n) throw new Error(`Unknown selection locator ${r}`);
                return t["label"](r)
            }
            async function g(e, t) {
                const r = e.map(e => t.emit(e));
                let n = [];
                return (await Promise.all(r)).forEach(e => {
                    "string" == typeof e && e.includes("\n") ? e.split("\n").forEach(e => {
                        n.push(e)
                    }) : n.push(e)
                }), n
            }
            async function f(e, {
                commandPrefixPadding: t,
                generateMethodDeclaration: r,
                terminatingKeyword: n,
                emitter: s,
                overrideCommandEmitting: i
            } = {}) {
                const o = r(e.name);
                let a, l = o,
                    c = n;
                return "object" == typeof o && (l = o.body, c = o.terminatingKeyword), [l, (a = i ? e.commands.map(e => `${t.repeat(e.level)+e.statement}`) : await g(e.commands, s)).join(`\n${t}`).replace(/^/, t), c]
            }

            function b(e, {
                commentPrefix: t
            }) {
                let r = [];
                return r.push(t + ` Test name: ${e.name}`), r.push(t + " Step # | name | target | value | comment"), e.commands.forEach((e, n) => {
                    r.push(t + ` ${n+1} | ${e.command} | ${e.target} | ${e.value} | ${e.comment}`)
                }), r
            }
            async function w(e, t, {
                testLevel: r,
                commandLevel: n,
                testDeclaration: s,
                terminatingKeyword: l,
                commandPrefixPadding: c,
                commentPrefix: m,
                hooks: d,
                emitter: p,
                generateMethodDeclaration: u,
                enableOriginTracing: h,
                project: w
            } = {}) {
                let A = {};
                r = r || 1, n = n || 2;
                const v = (0, a.findReusedTestMethods)(e, t),
                    y = i.default.bind(this, c);
                if (p.extras)
                    for (const r in p.extras) {
                        let n = !0;
                        if ("emitWaitForWindow" === r && (0, a.findCommandThatOpensWindow)(e, t) && (n = !1), !n) {
                            const e = await p.extras[r](),
                                t = await f(e, {
                                    emitter: p,
                                    commandPrefixPadding: c,
                                    generateMethodDeclaration: e.generateMethodDeclaration,
                                    terminatingKeyword: l,
                                    overrideCommandEmitting: !0
                                });
                            await (0, o.registerMethod)(e.name, t, {
                                generateMethodDeclaration: e.generateMethodDeclaration,
                                hooks: d
                            })
                        }
                    }
                for (const e of v) {
                    const t = await f(e, {
                        emitter: p,
                        commandPrefixPadding: c,
                        generateMethodDeclaration: u,
                        terminatingKeyword: l
                    });
                    await (0, o.registerMethod)(e.name, t, {
                        generateMethodDeclaration: u,
                        hooks: d
                    })
                }
                const x = h ? b(e, {
                    commentPrefix: m
                }) : void 0;
                return A.testDeclaration = y(s, {
                    startingLevel: r
                }), A.inEachBegin = y(await d.inEachBegin.emit({
                    test: e,
                    tests: t,
                    project: w,
                    isOptional: !0
                }), {
                    startingLevel: n
                }), A.commands = y(await g(e.commands, p).catch(t => {
                    throw new Error(`Test '${e.name}' has a problem: ${t.message}`)
                }), {
                    startingLevel: n,
                    originTracing: x
                }), A.inEachEnd = y(await d.inEachEnd.emit({
                    test: e,
                    tests: t,
                    project: w,
                    isOptional: !0
                }), {
                    startingLevel: n
                }), A.testEnd = y(l, {
                    startingLevel: r
                }), A
            }
            var A = {
                command: p,
                commands: g,
                location: u,
                method: f,
                selection: h,
                suite: async function(e, t, {
                    suiteLevel: r,
                    testLevel: n,
                    suiteName: s,
                    suiteDeclaration: o,
                    terminatingKeyword: a,
                    commandPrefixPadding: l,
                    commentPrefix: c,
                    hooks: m,
                    suite: d,
                    project: p,
                    beforeEachOptions: u
                } = {}) {
                    let h = {};
                    n = n || 1, r = r || 0, d || (d = {
                        name: s
                    });
                    const g = i.default.bind(this, l);
                    return h.headerComment = c + " Generated by Selenium IDE\n", h.dependencies = g(await m.declareDependencies.emit({
                        suite: d,
                        tests: t,
                        project: p
                    })), h.suiteDeclaration = g(o, {
                        startingLevel: r
                    }), h.variables = g(await m.declareVariables.emit({
                        suite: d,
                        tests: t,
                        project: p
                    }), {
                        startingLevel: n
                    }), h.beforeAll = g(await m.beforeAll.emit({
                        suite: d,
                        tests: t,
                        project: p,
                        isOptional: !0
                    }), {
                        startingLevel: n
                    }), h.beforeEach = g(await m.beforeEach.emit({
                        suite: d,
                        tests: t,
                        project: p,
                        startingSyntaxOptions: u
                    }), {
                        startingLevel: n
                    }), h.afterEach = g(await m.afterEach.emit({
                        suite: d,
                        tests: t,
                        project: p
                    }), {
                        startingLevel: n
                    }), h.afterAll = g(await m.afterAll.emit({
                        suite: d,
                        tests: t,
                        project: p,
                        isOptional: !0
                    }), {
                        startingLevel: n
                    }), h.methods = g(await m.declareMethods.emit({
                        suite: d,
                        tests: t,
                        project: p,
                        isOptional: !0
                    }), {
                        startingLevel: n
                    }), h.tests = e, h.suiteEnd = g(a, {
                        startingLevel: r
                    }), m.declareMethods.clearRegister(), h
                },
                orderedSuite: function(e) {
                    let t = "";
                    if (t += e.headerComment, t += e.dependencies, t += e.suiteDeclaration, t += e.variables, t += e.beforeAll, t += e.beforeEach, t += e.afterEach, t += e.afterAll, t += e.methods, e.tests.testDeclaration) {
                        const r = e.tests;
                        t += r.testDeclaration, t += r.inEachBegin, t += r.commands, t += r.inEachEnd, t += r.testEnd
                    } else
                        for (const r in e.tests) {
                            const n = e.tests[r];
                            t += n.testDeclaration, t += n.inEachBegin, t += n.commands, t += n.inEachEnd, t += n.testEnd
                        }
                    return t += e.suiteEnd
                },
                test: w,
                text: n.default,
                testsFromSuite: async function(e, t, r, {
                    enableOriginTracing: n,
                    generateTestDeclaration: s,
                    project: i
                }) {
                    let o = {};
                    for (const a of t.tests) {
                        const t = e.find(e => e.name === a),
                            l = s(t.name);
                        o[t.name] = await w(t, e, m({}, r, {
                            testDeclaration: l,
                            enableOriginTracing: n,
                            project: i
                        }))
                    }
                    return o
                }
            };
            t.default = A
        },
        94: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.registerCommand = function(e, t) {
                if (a.find(e => e[0] === t.command)) throw new Error("Unable to overwrite existing command");
                a.push([e, t])
            }, t.Commands = t.TargetTypes = void 0;
            var n = r(57);

            function s(e) {
                for (var t = 1; t < arguments.length; t++) {
                    var r = null != arguments[t] ? arguments[t] : {},
                        n = Object.keys(r);
                    "function" == typeof Object.getOwnPropertySymbols && (n = n.concat(Object.getOwnPropertySymbols(r).filter(function(e) {
                        return Object.getOwnPropertyDescriptor(r, e).enumerable
                    }))), n.forEach(function(t) {
                        i(e, t, r[t])
                    })
                }
                return e
            }

            function i(e, t, r) {
                return t in e ? Object.defineProperty(e, t, {
                    value: r,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[t] = r, e
            }
            const o = {
                NONE: 0,
                LOCATOR: "locator",
                REGION: "region"
            };
            t.TargetTypes = o;
            const a = [
                ["addSelection", {
                    name: "add selection",
                    description: `Add a selection to the set of options in a multi-select element.`,
                    type: o.LOCATOR,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.value
                }],
                ["answerOnNextPrompt", {
                    name: "answer on next prompt",
                    description: `Affects the next alert prompt. This command will send the \n        specified answer string to it. If the alert is already present, then use \n        "webdriver answer on visible prompt" instead.`,
                    target: n.ArgTypes.answer
                }],
                ["assert", {
                    name: "assert",
                    description: `Check that a variable is an expected value. The variable's \n        value will be converted to a string for comparison. The test will stop if the assert fails.`,
                    target: n.ArgTypes.variableName,
                    value: n.ArgTypes.expectedValue
                }],
                ["assertAlert", {
                    name: "assert alert",
                    description: `Confirm that an alert has been rendered with the provided text. The test will stop if the assert fails.`,
                    target: n.ArgTypes.alertText
                }],
                ["assertChecked", {
                    name: "assert checked",
                    type: o.LOCATOR,
                    description: "Confirm that the target element has been checked. The test will stop if the assert fails.",
                    target: n.ArgTypes.locator
                }],
                ["assertConfirmation", {
                    name: "assert confirmation",
                    description: "Confirm that a confirmation has been rendered. The test will stop if the assert fails.",
                    target: n.ArgTypes.text
                }],
                ["assertEditable", {
                    name: "assert editable",
                    type: o.LOCATOR,
                    description: "Confirm that the target element is editable. The test will stop if the assert fails.",
                    target: n.ArgTypes.locator
                }],
                ["assertElementPresent", {
                    name: "assert element present",
                    type: o.LOCATOR,
                    description: `Confirm that the target element is present somewhere on the page. The test will stop if the assert fails.`,
                    target: n.ArgTypes.locator
                }],
                ["assertElementNotPresent", {
                    name: "assert element not present",
                    type: o.LOCATOR,
                    description: `Confirm that the target element is not present anywhere on the page. The test will stop if the assert fails.`,
                    target: n.ArgTypes.locator
                }],
                ["assertNotChecked", {
                    name: "assert not checked",
                    type: o.LOCATOR,
                    description: "Confirm that the target element has not been checked. The test will stop if the assert fails.",
                    target: n.ArgTypes.locator
                }],
                ["assertNotEditable", {
                    name: "assert not editable",
                    type: o.LOCATOR,
                    description: "Confirm that the target element is not editable. The test will stop if the assert fails.",
                    target: n.ArgTypes.locator
                }],
                ["assertNotSelectedValue", {
                    name: "assert not selected value",
                    type: o.LOCATOR,
                    description: `Confirm that the value attribute of the selected option \n        in a dropdown element does not contain the provided value. The test will stop if the assert fails.`,
                    target: n.ArgTypes.selectLocator,
                    value: n.ArgTypes.pattern
                }],
                ["assertNotText", {
                    name: "assert not text",
                    type: o.LOCATOR,
                    description: `Confirm that the text of an element does not contain the provided value.\n      The test will stop if the assert fails.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.pattern
                }],
                ["assertPrompt", {
                    name: "assert prompt",
                    description: "Confirm that a JavaScript prompt has been rendered. The test will stop if the assert fails.",
                    target: n.ArgTypes.text
                }],
                ["assertSelectedValue", {
                    name: "assert selected value",
                    type: o.LOCATOR,
                    description: `Confirm that the value attribute of the selected option \n        in a dropdown element contains the provided value. The test will stop if the assert fails.`,
                    target: n.ArgTypes.selectLocator,
                    value: n.ArgTypes.pattern
                }],
                ["assertSelectedLabel", {
                    name: "assert selected label",
                    type: o.LOCATOR,
                    description: `Confirm that the label of the selected option in a dropdown \n        element contains the provided value. The test will stop if the assert fails.`,
                    target: n.ArgTypes.selectLocator,
                    value: n.ArgTypes.pattern
                }],
                ["assertText", {
                    name: "assert text",
                    type: o.LOCATOR,
                    description: `Confirm that the text of an element contains the provided value.\n      The test will stop if the assert fails.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.pattern
                }],
                ["assertTitle", {
                    name: "assert title",
                    description: `Confirm the title of the current page contains the provided text.\n      The test will stop if the assert fails.`,
                    target: n.ArgTypes.pattern
                }],
                ["assertValue", {
                    name: "assert value",
                    type: o.LOCATOR,
                    description: `Confirm the (whitespace-trimmed) value of an input field \n        (or anything else with a value parameter). For checkbox/radio elements, \n        the value will be "on" or "off" depending on whether the element is \n        checked or not. The test will stop if the assert fails.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.pattern
                }],
                ["check", {
                    name: "check",
                    type: o.LOCATOR,
                    description: "Check a toggle-button (checkbox/radio).",
                    target: n.ArgTypes.locator
                }],
                ["chooseCancelOnNextConfirmation", {
                    name: "choose cancel on next confirmation",
                    description: `Affects the next confirmation alert. This command will \n        cancel it. If the alert is already present, then use "webdriver choose \n        cancel on visible confirmation" instead.`
                }],
                ["chooseCancelOnNextPrompt", {
                    name: "choose cancel on next prompt",
                    description: `Affects the next alert prompt. This command will cancel \n        it. If the alert is already present, then use "webdriver choose cancel \n        on visible prompt" instead.`
                }],
                ["chooseOkOnNextConfirmation", {
                    name: "choose ok on next confirmation",
                    description: `Affects the next confirmation alert. This command will \n        accept it. If the alert is already present, then use "webdriver choose \n        ok on visible confirmation" instead.`
                }],
                ["click", {
                    name: "click",
                    type: o.LOCATOR,
                    description: `Clicks on a target element (e.g., a link, button, checkbox, or radio button).`,
                    target: n.ArgTypes.locator
                }],
                ["clickAt", {
                    name: "click at",
                    type: o.LOCATOR,
                    description: `Clicks on a target element (e.g., a link, button, checkbox, \n        or radio button). The coordinates are relative to the target element \n        (e.g., 0,0 is the top left corner of the element) and are mostly used \n        to check effects that relay on them, for example the material ripple effect.`,
                    target: n.ArgTypes.locator,
                    value: s({
                        isOptional: !0
                    }, n.ArgTypes.coord)
                }],
                ["close", {
                    name: "close",
                    description: `Closes the current window. There is no need to close the \n        initial window, IDE will re-use it; closing it may cause a performance \n        penalty on the test.`
                }],
                ["debugger", {
                    name: "debugger",
                    description: "Breaks the execution and enters debugger"
                }],
                ["do", {
                    name: "do",
                    description: `Create a loop that executes the proceeding commands at \n        least once. Terminate the branch with the repeat if command.`
                }],
                ["doubleClick", {
                    name: "double click",
                    type: o.LOCATOR,
                    description: `Double clicks on an element (e.g., a link, button, checkbox, or radio button).`,
                    target: n.ArgTypes.locator
                }],
                ["doubleClickAt", {
                    name: "double click at",
                    type: o.LOCATOR,
                    description: `Double clicks on a target element (e.g., a link, button, \n        checkbox, or radio button). The coordinates are relative to the target \n        element (e.g., 0,0 is the top left corner of the element) and are mostly \n        used to check effects that relay on them, for example the material \n        ripple effect.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.coord
                }],
                ["dragAndDropToObject", {
                    name: "drag and drop to object",
                    type: o.LOCATOR,
                    description: "Drags an element and drops it on another element.",
                    target: n.ArgTypes.locatorOfObjectToBeDragged,
                    value: n.ArgTypes.locatorOfDragDestinationObject
                }],
                ["echo", {
                    name: "echo",
                    description: `Prints the specified message into the third table cell in \n        your Selenese tables. Useful for debugging.`,
                    target: n.ArgTypes.message
                }],
                ["editContent", {
                    name: "edit content",
                    type: o.LOCATOR,
                    description: `Sets the value of a content editable element as if you typed in it.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.value
                }],
                ["else", {
                    name: "else",
                    description: `Part of an if block. Execute the commands in this branch \n        when an if and/or else if condition are not met. Terminate the branch \n        with the end command.`
                }],
                ["elseIf", {
                    name: "else if",
                    description: `Part of an if block. Execute the commands in this branch \n        when an if condition has not been met. Terminate the branch with the \n        end command.`,
                    target: n.ArgTypes.conditionalExpression
                }],
                ["end", {
                    name: "end",
                    description: `Terminates a control flow block for if, while, and times.`
                }],
                ["executeScript", {
                    name: "execute script",
                    description: `Executes a snippet of JavaScript in the context of the \n        currently selected frame or window. The script fragment will be executed \n        as the body of an anonymous function.  To store the return value, use \n        the 'return' keyword and provide a variable name in the value input field.`,
                    target: n.ArgTypes.script,
                    value: s({
                        isOptional: !0
                    }, n.ArgTypes.variableName)
                }],
                ["executeAsyncScript", {
                    name: "execute async script",
                    description: `Executes an async snippet of JavaScript in the context of \n        the currently selected frame or window. The script fragment will be \n        executed as the body of an anonymous function and must return a Promise. \n        The Promise result will be saved on the variable if you use the 'return' \n        keyword.`,
                    target: n.ArgTypes.script,
                    value: s({
                        isOptional: !0
                    }, n.ArgTypes.variableName)
                }],
                ["forEach", {
                    name: "for each",
                    description: `Create a loop that executes the proceeding commands for each item in a given collection.`,
                    target: n.ArgTypes.arrayVariableName,
                    value: n.ArgTypes.iteratorVariableName
                }],
                ["if", {
                    name: "if",
                    type: o.LOCATOR,
                    description: `Create a conditional branch in your test. Terminate the branch with the end command.`,
                    target: n.ArgTypes.conditionalExpression
                }],
                ["mouseDown", {
                    name: "mouse down",
                    type: o.LOCATOR,
                    description: `Simulates a user pressing the left mouse button (without \n        releasing it yet).`,
                    target: n.ArgTypes.locator
                }],
                ["mouseDownAt", {
                    name: "mouse down at",
                    type: o.LOCATOR,
                    description: `Simulates a user pressing the left mouse button (without \n        releasing it yet) at the specified location.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.coord
                }],
                ["mouseMoveAt", {
                    name: "mouse move at",
                    type: o.LOCATOR,
                    description: `Simulates a user pressing the mouse button (without releasing \n        it yet) on the specified element.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.coord
                }],
                ["mouseOut", {
                    name: "mouse out",
                    type: o.LOCATOR,
                    description: `Simulates a user moving the mouse pointer away from the specified element.`,
                    target: n.ArgTypes.locator
                }],
                ["mouseOver", {
                    name: "mouse over",
                    type: o.LOCATOR,
                    description: `Simulates a user hovering a mouse over the specified element.`,
                    target: n.ArgTypes.locator
                }],
                ["mouseUp", {
                    name: "mouse up",
                    type: o.LOCATOR,
                    description: `Simulates the event that occurs when the user releases the \n        mouse button (e.g., stops holding the button down).`,
                    target: n.ArgTypes.locator
                }],
                ["mouseUpAt", {
                    name: "mouse up at",
                    type: o.LOCATOR,
                    description: `Simulates the event that occurs when the user releases the \n        mouse button (e.g., stops holding the button down) at the specified location.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.coord
                }],
                ["open", {
                    name: "open",
                    description: `Opens a URL and waits for the page to load before proceeding. \n        This accepts both relative and absolute URLs.`,
                    target: n.ArgTypes.url
                }],
                ["pause", {
                    name: "pause",
                    description: "Wait for the specified amount of time.",
                    target: n.ArgTypes.waitTime
                }],
                ["removeSelection", {
                    name: "remove selection",
                    type: o.LOCATOR,
                    description: `Remove a selection from the set of selected options in a \n        multi-select element using an option locator.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.optionLocator
                }],
                ["repeatIf", {
                    name: "repeat if",
                    description: `Terminate a 'do' control flow branch conditionally. If \n        the result of the provided conditional expression is true, it starts \n        the do loop over.  Otherwise it ends the loop.`,
                    target: n.ArgTypes.conditionalExpression,
                    value: s({
                        isOptional: !0
                    }, n.ArgTypes.loopLimit)
                }],
                ["run", {
                    name: "run",
                    description: "Runs a test case from the current project.",
                    target: n.ArgTypes.testCase,
                    value: s({
                        isOptional: !0
                    }, n.ArgTypes.optionalFlag)
                }],
                ["runScript", {
                    name: "run script",
                    description: `Creates a new "script" tag in the body of the current \n        test window, and adds the specified text into the body of the command. \n        Beware that JS exceptions thrown in these script tags aren't managed \n        by Selenium, so you should probably wrap your script in try/catch blocks \n        if there is any chance that the script will throw an exception.`,
                    target: n.ArgTypes.script
                }],
                ["select", {
                    name: "select",
                    type: o.LOCATOR,
                    description: `Select an element from a drop-down menu using an option \n        locator. Option locators provide different ways of specifying a select \n        element (e.g., label=, value=, id=, index=). If no option locator prefix \n        is provided, a match on the label will be attempted.`,
                    target: n.ArgTypes.selectLocator,
                    value: n.ArgTypes.optionLocator
                }],
                ["selectFrame", {
                    name: "select frame",
                    type: o.LOCATOR,
                    description: `Selects a frame within the current window. You can select a\n        frame by its 0-based index number (e.g., select the first frame with \n        "index=0", or the third frame with "index=2"). For nested frames you will\n        need to invoke this command multiple times (once for each frame in the \n        tree until you reach your desired frame). You can select the parent \n        frame with "relative=parent". To return to the top of the page use \n        "relative=top".`,
                    target: n.ArgTypes.locator
                }],
                ["selectWindow", {
                    name: "select window",
                    description: `Selects a popup window using a window locator. Once a \n        popup window has been selected, all commands will go to that window. \n        Window locators use handles to select windows.`,
                    target: n.ArgTypes.handle
                }],
                ["sendKeys", {
                    name: "send keys",
                    type: o.LOCATOR,
                    description: `Simulates keystroke events on the specified element, as \n        though you typed the value key-by-key. This simulates a real user typing \n        every character in the specified string; it is also bound by the \n        limitations of a real user, like not being able to type into a invisible \n        or read only elements.  This is useful for dynamic UI widgets (like \n        auto-completing combo boxes) that require explicit key events. Unlike \n        the simple "type" command, which forces the specified value into the \n        page directly, this command will not replace the existing content.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.keySequence
                }],
                ["setSpeed", {
                    name: "set speed",
                    description: `Set execution speed (e.g., set the millisecond length of \n        a delay which will follow each Selenium operation). By default, there \n        is no such delay, e.g., the delay is 0 milliseconds. This setting is \n        global, and will affect all test runs, until changed.`,
                    target: n.ArgTypes.waitTime
                }],
                ["setWindowSize", {
                    name: "set window size",
                    description: "Set the browser's window size, including the browser's interface.",
                    target: n.ArgTypes.resolution
                }],
                ["store", {
                    name: "store",
                    description: "Save a target string as a variable for easy re-use.",
                    target: n.ArgTypes.text,
                    value: n.ArgTypes.variableName
                }],
                ["storeAttribute", {
                    name: "store attribute",
                    description: `Gets the value of an element attribute. The value of the \n        attribute may differ across browsers (this is the case for the "style" \n        attribute, for example).`,
                    target: n.ArgTypes.attributeLocator,
                    value: n.ArgTypes.variableName
                }],
                ["storeJson", {
                    name: "store json",
                    description: ``,
                    target: n.ArgTypes.json,
                    value: n.ArgTypes.variableName
                }],
                ["storeText", {
                    name: "store text",
                    type: o.LOCATOR,
                    description: `Gets the text of an element and stores it for later use. \n        This works for any element that contains text.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.variableName
                }],
                ["storeTitle", {
                    name: "store title",
                    description: "Gets the title of the current page.",
                    target: n.ArgTypes.text,
                    value: n.ArgTypes.variableName
                }],
                ["storeValue", {
                    name: "store value",
                    type: o.LOCATOR,
                    description: `Gets the value of element and stores it for later use. \n        This works for any input type element.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.variableName
                }],
                ["storeWindowHandle", {
                    name: "store window handle",
                    description: "Gets the handle of the current page.",
                    target: n.ArgTypes.handle
                }],
                ["storeXpathCount", {
                    name: "store xpath count",
                    description: `Gets the number of nodes that match the specified xpath \n        (e.g. "//table" would give the number of tables).`,
                    target: n.ArgTypes.xpath,
                    value: n.ArgTypes.variableName
                }],
                ["submit", {
                    name: "submit",
                    type: o.LOCATOR,
                    description: `Submit the specified form. This is particularly useful for \n        forms without submit buttons, e.g. single-input "Search" forms.`,
                    target: n.ArgTypes.formLocator
                }],
                ["times", {
                    name: "times",
                    description: `Create a loop that executes the proceeding commands n number of times.`,
                    target: n.ArgTypes.times,
                    value: s({
                        isOptional: !0
                    }, n.ArgTypes.loopLimit)
                }],
                ["type", {
                    name: "type",
                    type: o.LOCATOR,
                    description: `Sets the value of an input field, as though you typed it \n        in. Can also be used to set the value of combo boxes, check boxes, etc. \n        In these cases, value should be the value of the option selected, not \n        the visible text.  Chrome only: If a file path is given it will be \n        uploaded to the input (for type=file), NOTE: XPath locators are not \n        supported.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.value
                }],
                ["uncheck", {
                    name: "uncheck",
                    type: o.LOCATOR,
                    description: "Uncheck a toggle-button (checkbox/radio).",
                    target: n.ArgTypes.locator
                }],
                ["verify", {
                    name: "verify",
                    description: `Soft assert that a variable is an expected value. The \n        variable's value will be converted to a string for comparison.\n        The test will continue even if the verify fails.`,
                    target: n.ArgTypes.variableName,
                    value: n.ArgTypes.expectedValue
                }],
                ["verifyChecked", {
                    name: "verify checked",
                    type: o.LOCATOR,
                    description: `Soft assert that a toggle-button (checkbox/radio) has been checked.\n      The test will continue even if the verify fails.`,
                    target: n.ArgTypes.locator
                }],
                ["verifyEditable", {
                    name: "verify editable",
                    type: o.LOCATOR,
                    description: `Soft assert whether the specified input element is \n        editable (e.g., hasn't been disabled). The test will continue even if the verify fails.`,
                    target: n.ArgTypes.locator
                }],
                ["verifyElementPresent", {
                    name: "verify element present",
                    type: o.LOCATOR,
                    description: `Soft assert that the specified element is somewhere on the page.\n      The test will continue even if the verify fails.`,
                    target: n.ArgTypes.locator
                }],
                ["verifyElementNotPresent", {
                    name: "verify element not present",
                    type: o.LOCATOR,
                    description: `Soft assert that the specified element is not somewhere on the page.\n      The test will continue even if the verify fails.`,
                    target: n.ArgTypes.locator
                }],
                ["verifyNotChecked", {
                    name: "verify not checked",
                    type: o.LOCATOR,
                    description: `Soft assert that a toggle-button (checkbox/radio) has not been checked.\n      The test will continue even if the verify fails.`,
                    target: n.ArgTypes.locator
                }],
                ["verifyNotEditable", {
                    name: "verify not editable",
                    type: o.LOCATOR,
                    description: `Soft assert whether the specified input element is not \n        editable (e.g., hasn't been disabled). The test will continue even if the verify fails.`,
                    target: n.ArgTypes.locator
                }],
                ["verifyNotSelectedValue", {
                    name: "verify not selected value",
                    description: `Soft assert that the expected element has not been chosen \n        in a select menu by its option attribute. The test will continue even if the verify fails.`,
                    target: n.ArgTypes.selectLocator,
                    value: n.ArgTypes.optionLocator
                }],
                ["verifyNotText", {
                    name: "verify not text",
                    type: o.LOCATOR,
                    description: "Soft assert the text of an element is not present. The test will continue even if the verify fails.",
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.text
                }],
                ["verifySelectedLabel", {
                    name: "verify selected label",
                    type: o.LOCATOR,
                    description: `Soft assert the visible text for a selected option in the \n        specified select element. The test will continue even if the verify fails.`,
                    target: n.ArgTypes.selectLocator,
                    value: n.ArgTypes.pattern
                }],
                ["verifySelectedValue", {
                    name: "verify selected value",
                    type: o.LOCATOR,
                    description: `Soft assert that the expected element has been chosen in \n        a select menu by its option attribute. The test will continue even if the verify fails.`,
                    target: n.ArgTypes.selectLocator,
                    value: n.ArgTypes.optionLocator
                }],
                ["verifyText", {
                    name: "verify text",
                    type: o.LOCATOR,
                    description: "Soft assert the text of an element is present. The test will continue even if the verify fails.",
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.text
                }],
                ["verifyTitle", {
                    name: "verify title",
                    description: "Soft assert the title of the current page contains the provided text. The test will continue even if the verify fails.",
                    target: n.ArgTypes.text
                }],
                ["verifyValue", {
                    name: "verify value",
                    type: o.LOCATOR,
                    description: `Soft assert the (whitespace-trimmed) value of an input \n        field (or anything else with a value parameter). For checkbox/radio \n        elements, the value will be "on" or "off" depending on whether the \n        element is checked or not. The test will continue even if the verify fails.`,
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.pattern
                }],
                ["waitForElementEditable", {
                    name: "wait for element editable",
                    type: o.LOCATOR,
                    description: "Wait for an element to be editable.",
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.waitTime
                }],
                ["waitForElementNotEditable", {
                    name: "wait for element not editable",
                    type: o.LOCATOR,
                    description: "Wait for an element to not be editable.",
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.waitTime
                }],
                ["waitForElementNotPresent", {
                    name: "wait for element not present",
                    type: o.LOCATOR,
                    description: "Wait for a target element to not be present on the page.",
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.waitTime
                }],
                ["waitForElementNotVisible", {
                    name: "wait for element not visible",
                    type: o.LOCATOR,
                    description: "Wait for a target element to not be visible on the page.",
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.waitTime
                }],
                ["waitForElementPresent", {
                    name: "wait for element present",
                    type: o.LOCATOR,
                    description: "Wait for a target element to be present on the page.",
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.waitTime
                }],
                ["waitForElementVisible", {
                    name: "wait for element visible",
                    type: o.LOCATOR,
                    description: "Wait for a target element to be visible on the page.",
                    target: n.ArgTypes.locator,
                    value: n.ArgTypes.waitTime
                }],
                ["webdriverAnswerOnVisiblePrompt", {
                    name: "webdriver answer on visible prompt",
                    description: `Affects a currently showing alert prompt. This command \n        instructs Selenium to provide the specified answer to it. If the alert \n        has not appeared yet then use "answer on next prompt" instead.`,
                    target: n.ArgTypes.answer
                }],
                ["webdriverChooseCancelOnVisibleConfirmation", {
                    name: "webdriver choose cancel on visible confirmation",
                    description: `Affects a currently showing confirmation alert. This \n        command instructs Selenium to cancel it. If the alert has not appeared \n        yet then use "choose cancel on next confirmation" instead.`
                }],
                ["webdriverChooseCancelOnVisiblePrompt", {
                    name: "webdriver choose cancel on visible prompt",
                    description: `Affects a currently showing alert prompt. This command \n        instructs Selenium to cancel it. If the alert has not appeared yet \n        then use "choose cancel on next prompt" instead.`
                }],
                ["webdriverChooseOkOnVisibleConfirmation", {
                    name: "webdriver choose ok on visible confirmation",
                    description: `Affects a currently showing confirmation alert. This \n        command instructs Selenium to accept it. If the alert has not appeared \n        yet then use "choose ok on next confirmation" instead.`
                }],
                ["while", {
                    name: "while",
                    description: `Create a loop that executes the proceeding commands \n        repeatedly for as long as the provided conditional expression is true.`,
                    target: n.ArgTypes.conditionalExpression,
                    value: s({
                        isOptional: !0
                    }, n.ArgTypes.loopLimit)
                }]
            ];
            t.Commands = a
        },
        95: function(e, t, r) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.clearHooks = function(e) {
                Object.keys(e).forEach(t => {
                    e[t].clearRegister()
                })
            }, t.default = void 0;
            t.default = class {
                constructor({
                    startingSyntax: e,
                    endingSyntax: t,
                    registrationLevel: r
                } = {}) {
                    this.startingSyntax = e, this.endingSyntax = t, this.registrationLevel = r, this.clearRegister = this.clearRegister.bind(this), this.emit = this.emit.bind(this), this.register = this.register.bind(this), this.isRegistered = this.isRegistered.bind(this), this.clearRegister()
                }
                clearRegister() {
                    this.emitters = []
                }
                async emit({
                    isOptional: e,
                    test: t,
                    suite: r,
                    tests: n,
                    project: s,
                    startingSyntaxOptions: i
                } = {
                    isOptional: !1
                }) {
                    const o = [];
                    let a = 0;
                    if (this.startingSyntax) {
                        const e = "function" == typeof this.startingSyntax ? this.startingSyntax(i) : this.startingSyntax;
                        e.commands ? e.commands.forEach(e => {
                            o.push(e), a = e.level
                        }) : o.push({
                            level: 0,
                            statement: e
                        })
                    }
                    const l = t ? t.name : r ? r.name : void 0,
                        c = (await Promise.all(this.emitters.map(e => e({
                            name: l,
                            tests: n,
                            project: s
                        })))).filter(e => void 0 != e);
                    if (!e || c.length) return c.forEach(e => {
                        "object" == typeof e ? o.push(e) : "string" == typeof e && e.split("\n").forEach(e => {
                            o.push({
                                level: this.registrationLevel ? this.registrationLevel : a,
                                statement: e
                            })
                        })
                    }), this.endingSyntax && (this.endingSyntax.commands ? this.endingSyntax.commands.forEach(e => {
                        o.push(e)
                    }) : o.push({
                        level: 0,
                        statement: this.endingSyntax
                    })), {
                        commands: o
                    }
                }
                register(e) {
                    this.emitters.push(e)
                }
                async isRegistered(e = "") {
                    return (await Promise.all(this.emitters.map(e => e()))).includes(e)
                }
            }
        },
        96: function(e, t, r) {
            "use strict";

            function n(e) {
                return e.replace(/([^a-z0-9]+)/gi, "")
            }

            function s(e) {
                return e.charAt(0).toUpperCase() + e.substr(1)
            }

            function i(e) {
                return e.charAt(0).toLowerCase() + e.substr(1)
            }
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.sanitizeName = n, t.capitalize = s, t.uncapitalize = i, t.default = void 0;
            var o = {
                sanitizeName: n,
                capitalize: s,
                uncapitalize: i
            };
            t.default = o
        },
        97: function(e, t, r) {
            "use strict";
            var n, s = (n = r(98)) && n.__esModule ? n : {
                default: n
            };
            const i = (() => {
                    try {
                        return (0, s.default)(window.navigator.userAgent)
                    } catch (e) {
                        return !1
                    }
                })(),
                o = i && i.browser ? i.browser : void 0,
                a = o && "Chrome" === o.name,
                l = o && "Firefox" === o.name,
                c = a || l ? o.name : void 0;
            e.exports = {
                userAgent: i,
                browserName: c,
                isChrome: a,
                isFirefox: l
            }
        },
        98: function(e, t, r) {
            var n;
            ! function(s, i) {
                "use strict";
                var o = "",
                    a = "?",
                    l = "function",
                    c = "undefined",
                    m = "object",
                    d = "string",
                    p = "model",
                    u = "name",
                    h = "type",
                    g = "vendor",
                    f = "version",
                    b = "architecture",
                    w = "console",
                    A = "mobile",
                    v = "tablet",
                    y = "smarttv",
                    x = "wearable",
                    T = {
                        extend: function(e, t) {
                            var r = {};
                            for (var n in e) t[n] && t[n].length % 2 == 0 ? r[n] = t[n].concat(e[n]) : r[n] = e[n];
                            return r
                        },
                        has: function(e, t) {
                            return "string" == typeof e && -1 !== t.toLowerCase().indexOf(e.toLowerCase())
                        },
                        lowerize: function(e) {
                            return e.toLowerCase()
                        },
                        major: function(e) {
                            return typeof e === d ? e.replace(/[^\d\.]/g, "").split(".")[0] : i
                        },
                        trim: function(e) {
                            return e.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "")
                        }
                    },
                    O = {
                        rgx: function(e, t) {
                            for (var r, n, s, o, a, c, d = 0; d < t.length && !a;) {
                                var p = t[d],
                                    u = t[d + 1];
                                for (r = n = 0; r < p.length && !a;)
                                    if (a = p[r++].exec(e))
                                        for (s = 0; s < u.length; s++) c = a[++n], typeof(o = u[s]) === m && o.length > 0 ? 2 == o.length ? typeof o[1] == l ? this[o[0]] = o[1].call(this, c) : this[o[0]] = o[1] : 3 == o.length ? typeof o[1] !== l || o[1].exec && o[1].test ? this[o[0]] = c ? c.replace(o[1], o[2]) : i : this[o[0]] = c ? o[1].call(this, c, o[2]) : i : 4 == o.length && (this[o[0]] = c ? o[3].call(this, c.replace(o[1], o[2])) : i) : this[o] = c || i;
                                d += 2
                            }
                        },
                        str: function(e, t) {
                            for (var r in t)
                                if (typeof t[r] === m && t[r].length > 0) {
                                    for (var n = 0; n < t[r].length; n++)
                                        if (T.has(t[r][n], e)) return r === a ? i : r
                                } else if (T.has(t[r], e)) return r === a ? i : r;
                            return e
                        }
                    },
                    k = {
                        browser: {
                            oldsafari: {
                                version: {
                                    "1.0": "/8",
                                    1.2: "/1",
                                    1.3: "/3",
                                    "2.0": "/412",
                                    "2.0.2": "/416",
                                    "2.0.3": "/417",
                                    "2.0.4": "/419",
                                    "?": "/"
                                }
                            }
                        },
                        device: {
                            amazon: {
                                model: {
                                    "Fire Phone": ["SD", "KF"]
                                }
                            },
                            sprint: {
                                model: {
                                    "Evo Shift 4G": "7373KT"
                                },
                                vendor: {
                                    HTC: "APA",
                                    Sprint: "Sprint"
                                }
                            }
                        },
                        os: {
                            windows: {
                                version: {
                                    ME: "4.90",
                                    "NT 3.11": "NT3.51",
                                    "NT 4.0": "NT4.0",
                                    2000: "NT 5.0",
                                    XP: ["NT 5.1", "NT 5.2"],
                                    Vista: "NT 6.0",
                                    7: "NT 6.1",
                                    8: "NT 6.2",
                                    8.1: "NT 6.3",
                                    10: ["NT 6.4", "NT 10.0"],
                                    RT: "ARM"
                                }
                            }
                        }
                    },
                    C = {
                        browser: [
                            [/(opera\smini)\/([\w\.-]+)/i, /(opera\s[mobiletab]+).+version\/([\w\.-]+)/i, /(opera).+version\/([\w\.]+)/i, /(opera)[\/\s]+([\w\.]+)/i],
                            [u, f],
                            [/(opios)[\/\s]+([\w\.]+)/i],
                            [
                                [u, "Opera Mini"], f
                            ],
                            [/\s(opr)\/([\w\.]+)/i],
                            [
                                [u, "Opera"], f
                            ],
                            [/(kindle)\/([\w\.]+)/i, /(lunascape|maxthon|netfront|jasmine|blazer)[\/\s]?([\w\.]*)/i, /(avant\s|iemobile|slim|baidu)(?:browser)?[\/\s]?([\w\.]*)/i, /(?:ms|\()(ie)\s([\w\.]+)/i, /(rekonq)\/([\w\.]*)/i, /(chromium|flock|rockmelt|midori|epiphany|silk|skyfire|ovibrowser|bolt|iron|vivaldi|iridium|phantomjs|bowser|quark|qupzilla|falkon)\/([\w\.-]+)/i],
                            [u, f],
                            [/(konqueror)\/([\w\.]+)/i],
                            [
                                [u, "Konqueror"], f
                            ],
                            [/(trident).+rv[:\s]([\w\.]+).+like\sgecko/i],
                            [
                                [u, "IE"], f
                            ],
                            [/(edge|edgios|edga|edg)\/((\d+)?[\w\.]+)/i],
                            [
                                [u, "Edge"], f
                            ],
                            [/(yabrowser)\/([\w\.]+)/i],
                            [
                                [u, "Yandex"], f
                            ],
                            [/(puffin)\/([\w\.]+)/i],
                            [
                                [u, "Puffin"], f
                            ],
                            [/(focus)\/([\w\.]+)/i],
                            [
                                [u, "Firefox Focus"], f
                            ],
                            [/(opt)\/([\w\.]+)/i],
                            [
                                [u, "Opera Touch"], f
                            ],
                            [/((?:[\s\/])uc?\s?browser|(?:juc.+)ucweb)[\/\s]?([\w\.]+)/i],
                            [
                                [u, "UCBrowser"], f
                            ],
                            [/(comodo_dragon)\/([\w\.]+)/i],
                            [
                                [u, /_/g, " "], f
                            ],
                            [/(windowswechat qbcore)\/([\w\.]+)/i],
                            [
                                [u, "WeChat(Win) Desktop"], f
                            ],
                            [/(micromessenger)\/([\w\.]+)/i],
                            [
                                [u, "WeChat"], f
                            ],
                            [/(brave)\/([\w\.]+)/i],
                            [
                                [u, "Brave"], f
                            ],
                            [/(qqbrowserlite)\/([\w\.]+)/i],
                            [u, f],
                            [/(QQ)\/([\d\.]+)/i],
                            [u, f],
                            [/m?(qqbrowser)[\/\s]?([\w\.]+)/i],
                            [u, f],
                            [/(BIDUBrowser)[\/\s]?([\w\.]+)/i],
                            [u, f],
                            [/(2345Explorer)[\/\s]?([\w\.]+)/i],
                            [u, f],
                            [/(MetaSr)[\/\s]?([\w\.]+)/i],
                            [u],
                            [/(LBBROWSER)/i],
                            [u],
                            [/xiaomi\/miuibrowser\/([\w\.]+)/i],
                            [f, [u, "MIUI Browser"]],
                            [/;fbav\/([\w\.]+);/i],
                            [f, [u, "Facebook"]],
                            [/safari\s(line)\/([\w\.]+)/i, /android.+(line)\/([\w\.]+)\/iab/i],
                            [u, f],
                            [/headlesschrome(?:\/([\w\.]+)|\s)/i],
                            [f, [u, "Chrome Headless"]],
                            [/\swv\).+(chrome)\/([\w\.]+)/i],
                            [
                                [u, /(.+)/, "$1 WebView"], f
                            ],
                            [/((?:oculus|samsung)browser)\/([\w\.]+)/i],
                            [
                                [u, /(.+(?:g|us))(.+)/, "$1 $2"], f
                            ],
                            [/android.+version\/([\w\.]+)\s+(?:mobile\s?safari|safari)*/i],
                            [f, [u, "Android Browser"]],
                            [/(sailfishbrowser)\/([\w\.]+)/i],
                            [
                                [u, "Sailfish Browser"], f
                            ],
                            [/(chrome|omniweb|arora|[tizenoka]{5}\s?browser)\/v?([\w\.]+)/i],
                            [u, f],
                            [/(dolfin)\/([\w\.]+)/i],
                            [
                                [u, "Dolphin"], f
                            ],
                            [/((?:android.+)crmo|crios)\/([\w\.]+)/i],
                            [
                                [u, "Chrome"], f
                            ],
                            [/(coast)\/([\w\.]+)/i],
                            [
                                [u, "Opera Coast"], f
                            ],
                            [/fxios\/([\w\.-]+)/i],
                            [f, [u, "Firefox"]],
                            [/version\/([\w\.]+).+?mobile\/\w+\s(safari)/i],
                            [f, [u, "Mobile Safari"]],
                            [/version\/([\w\.]+).+?(mobile\s?safari|safari)/i],
                            [f, u],
                            [/webkit.+?(gsa)\/([\w\.]+).+?(mobile\s?safari|safari)(\/[\w\.]+)/i],
                            [
                                [u, "GSA"], f
                            ],
                            [/webkit.+?(mobile\s?safari|safari)(\/[\w\.]+)/i],
                            [u, [f, O.str, k.browser.oldsafari.version]],
                            [/(webkit|khtml)\/([\w\.]+)/i],
                            [u, f],
                            [/(navigator|netscape)\/([\w\.-]+)/i],
                            [
                                [u, "Netscape"], f
                            ],
                            [/(swiftfox)/i, /(icedragon|iceweasel|camino|chimera|fennec|maemo\sbrowser|minimo|conkeror)[\/\s]?([\w\.\+]+)/i, /(firefox|seamonkey|k-meleon|icecat|iceape|firebird|phoenix|palemoon|basilisk|waterfox)\/([\w\.-]+)$/i, /(mozilla)\/([\w\.]+).+rv\:.+gecko\/\d+/i, /(polaris|lynx|dillo|icab|doris|amaya|w3m|netsurf|sleipnir)[\/\s]?([\w\.]+)/i, /(links)\s\(([\w\.]+)/i, /(gobrowser)\/?([\w\.]*)/i, /(ice\s?browser)\/v?([\w\._]+)/i, /(mosaic)[\/\s]([\w\.]+)/i],
                            [u, f]
                        ],
                        cpu: [
                            [/(?:(amd|x(?:(?:86|64)[_-])?|wow|win)64)[;\)]/i],
                            [
                                [b, "amd64"]
                            ],
                            [/(ia32(?=;))/i],
                            [
                                [b, T.lowerize]
                            ],
                            [/((?:i[346]|x)86)[;\)]/i],
                            [
                                [b, "ia32"]
                            ],
                            [/windows\s(ce|mobile);\sppc;/i],
                            [
                                [b, "arm"]
                            ],
                            [/((?:ppc|powerpc)(?:64)?)(?:\smac|;|\))/i],
                            [
                                [b, /ower/, "", T.lowerize]
                            ],
                            [/(sun4\w)[;\)]/i],
                            [
                                [b, "sparc"]
                            ],
                            [/((?:avr32|ia64(?=;))|68k(?=\))|arm(?:64|(?=v\d+[;l]))|(?=atmel\s)avr|(?:irix|mips|sparc)(?:64)?(?=;)|pa-risc)/i],
                            [
                                [b, T.lowerize]
                            ]
                        ],
                        device: [
                            [/\((ipad|playbook);[\w\s\),;-]+(rim|apple)/i],
                            [p, g, [h, v]],
                            [/applecoremedia\/[\w\.]+ \((ipad)/],
                            [p, [g, "Apple"],
                                [h, v]
                            ],
                            [/(apple\s{0,1}tv)/i],
                            [
                                [p, "Apple TV"],
                                [g, "Apple"]
                            ],
                            [/(archos)\s(gamepad2?)/i, /(hp).+(touchpad)/i, /(hp).+(tablet)/i, /(kindle)\/([\w\.]+)/i, /\s(nook)[\w\s]+build\/(\w+)/i, /(dell)\s(strea[kpr\s\d]*[\dko])/i],
                            [g, p, [h, v]],
                            [/(kf[A-z]+)\sbuild\/.+silk\//i],
                            [p, [g, "Amazon"],
                                [h, v]
                            ],
                            [/(sd|kf)[0349hijorstuw]+\sbuild\/.+silk\//i],
                            [
                                [p, O.str, k.device.amazon.model],
                                [g, "Amazon"],
                                [h, A]
                            ],
                            [/android.+aft([bms])\sbuild/i],
                            [p, [g, "Amazon"],
                                [h, y]
                            ],
                            [/\((ip[honed|\s\w*]+);.+(apple)/i],
                            [p, g, [h, A]],
                            [/\((ip[honed|\s\w*]+);/i],
                            [p, [g, "Apple"],
                                [h, A]
                            ],
                            [/(blackberry)[\s-]?(\w+)/i, /(blackberry|benq|palm(?=\-)|sonyericsson|acer|asus|dell|meizu|motorola|polytron)[\s_-]?([\w-]*)/i, /(hp)\s([\w\s]+\w)/i, /(asus)-?(\w+)/i],
                            [g, p, [h, A]],
                            [/\(bb10;\s(\w+)/i],
                            [p, [g, "BlackBerry"],
                                [h, A]
                            ],
                            [/android.+(transfo[prime\s]{4,10}\s\w+|eeepc|slider\s\w+|nexus 7|padfone|p00c)/i],
                            [p, [g, "Asus"],
                                [h, v]
                            ],
                            [/(sony)\s(tablet\s[ps])\sbuild\//i, /(sony)?(?:sgp.+)\sbuild\//i],
                            [
                                [g, "Sony"],
                                [p, "Xperia Tablet"],
                                [h, v]
                            ],
                            [/android.+\s([c-g]\d{4}|so[-l]\w+)(?=\sbuild\/|\).+chrome\/(?![1-6]{0,1}\d\.))/i],
                            [p, [g, "Sony"],
                                [h, A]
                            ],
                            [/\s(ouya)\s/i, /(nintendo)\s([wids3u]+)/i],
                            [g, p, [h, w]],
                            [/android.+;\s(shield)\sbuild/i],
                            [p, [g, "Nvidia"],
                                [h, w]
                            ],
                            [/(playstation\s[34portablevi]+)/i],
                            [p, [g, "Sony"],
                                [h, w]
                            ],
                            [/(sprint\s(\w+))/i],
                            [
                                [g, O.str, k.device.sprint.vendor],
                                [p, O.str, k.device.sprint.model],
                                [h, A]
                            ],
                            [/(htc)[;_\s-]+([\w\s]+(?=\)|\sbuild)|\w+)/i, /(zte)-(\w*)/i, /(alcatel|geeksphone|nexian|panasonic|(?=;\s)sony)[_\s-]?([\w-]*)/i],
                            [g, [p, /_/g, " "],
                                [h, A]
                            ],
                            [/(nexus\s9)/i],
                            [p, [g, "HTC"],
                                [h, v]
                            ],
                            [/d\/huawei([\w\s-]+)[;\)]/i, /(nexus\s6p)/i],
                            [p, [g, "Huawei"],
                                [h, A]
                            ],
                            [/(microsoft);\s(lumia[\s\w]+)/i],
                            [g, p, [h, A]],
                            [/[\s\(;](xbox(?:\sone)?)[\s\);]/i],
                            [p, [g, "Microsoft"],
                                [h, w]
                            ],
                            [/(kin\.[onetw]{3})/i],
                            [
                                [p, /\./g, " "],
                                [g, "Microsoft"],
                                [h, A]
                            ],
                            [/\s(milestone|droid(?:[2-4x]|\s(?:bionic|x2|pro|razr))?:?(\s4g)?)[\w\s]+build\//i, /mot[\s-]?(\w*)/i, /(XT\d{3,4}) build\//i, /(nexus\s6)/i],
                            [p, [g, "Motorola"],
                                [h, A]
                            ],
                            [/android.+\s(mz60\d|xoom[\s2]{0,2})\sbuild\//i],
                            [p, [g, "Motorola"],
                                [h, v]
                            ],
                            [/hbbtv\/\d+\.\d+\.\d+\s+\([\w\s]*;\s*(\w[^;]*);([^;]*)/i],
                            [
                                [g, T.trim],
                                [p, T.trim],
                                [h, y]
                            ],
                            [/hbbtv.+maple;(\d+)/i],
                            [
                                [p, /^/, "SmartTV"],
                                [g, "Samsung"],
                                [h, y]
                            ],
                            [/\(dtv[\);].+(aquos)/i],
                            [p, [g, "Sharp"],
                                [h, y]
                            ],
                            [/android.+((sch-i[89]0\d|shw-m380s|gt-p\d{4}|gt-n\d+|sgh-t8[56]9|nexus 10))/i, /((SM-T\w+))/i],
                            [
                                [g, "Samsung"], p, [h, v]
                            ],
                            [/smart-tv.+(samsung)/i],
                            [g, [h, y], p],
                            [/((s[cgp]h-\w+|gt-\w+|galaxy\snexus|sm-\w[\w\d]+))/i, /(sam[sung]*)[\s-]*(\w+-?[\w-]*)/i, /sec-((sgh\w+))/i],
                            [
                                [g, "Samsung"], p, [h, A]
                            ],
                            [/sie-(\w*)/i],
                            [p, [g, "Siemens"],
                                [h, A]
                            ],
                            [/(maemo|nokia).*(n900|lumia\s\d+)/i, /(nokia)[\s_-]?([\w-]*)/i],
                            [
                                [g, "Nokia"], p, [h, A]
                            ],
                            [/android[x\d\.\s;]+\s([ab][1-7]\-?[0178a]\d\d?)/i],
                            [p, [g, "Acer"],
                                [h, v]
                            ],
                            [/android.+([vl]k\-?\d{3})\s+build/i],
                            [p, [g, "LG"],
                                [h, v]
                            ],
                            [/android\s3\.[\s\w;-]{10}(lg?)-([06cv9]{3,4})/i],
                            [
                                [g, "LG"], p, [h, v]
                            ],
                            [/(lg) netcast\.tv/i],
                            [g, p, [h, y]],
                            [/(nexus\s[45])/i, /lg[e;\s\/-]+(\w*)/i, /android.+lg(\-?[\d\w]+)\s+build/i],
                            [p, [g, "LG"],
                                [h, A]
                            ],
                            [/(lenovo)\s?(s(?:5000|6000)(?:[\w-]+)|tab(?:[\s\w]+))/i],
                            [g, p, [h, v]],
                            [/android.+(ideatab[a-z0-9\-\s]+)/i],
                            [p, [g, "Lenovo"],
                                [h, v]
                            ],
                            [/(lenovo)[_\s-]?([\w-]+)/i],
                            [g, p, [h, A]],
                            [/linux;.+((jolla));/i],
                            [g, p, [h, A]],
                            [/((pebble))app\/[\d\.]+\s/i],
                            [g, p, [h, x]],
                            [/android.+;\s(oppo)\s?([\w\s]+)\sbuild/i],
                            [g, p, [h, A]],
                            [/crkey/i],
                            [
                                [p, "Chromecast"],
                                [g, "Google"]
                            ],
                            [/android.+;\s(glass)\s\d/i],
                            [p, [g, "Google"],
                                [h, x]
                            ],
                            [/android.+;\s(pixel c)[\s)]/i],
                            [p, [g, "Google"],
                                [h, v]
                            ],
                            [/android.+;\s(pixel( [23])?( xl)?)[\s)]/i],
                            [p, [g, "Google"],
                                [h, A]
                            ],
                            [/android.+;\s(\w+)\s+build\/hm\1/i, /android.+(hm[\s\-_]*note?[\s_]*(?:\d\w)?)\s+build/i, /android.+(mi[\s\-_]*(?:a\d|one|one[\s_]plus|note lte)?[\s_]*(?:\d?\w?)[\s_]*(?:plus)?)\s+build/i, /android.+(redmi[\s\-_]*(?:note)?(?:[\s_]*[\w\s]+))\s+build/i],
                            [
                                [p, /_/g, " "],
                                [g, "Xiaomi"],
                                [h, A]
                            ],
                            [/android.+(mi[\s\-_]*(?:pad)(?:[\s_]*[\w\s]+))\s+build/i],
                            [
                                [p, /_/g, " "],
                                [g, "Xiaomi"],
                                [h, v]
                            ],
                            [/android.+;\s(m[1-5]\snote)\sbuild/i],
                            [p, [g, "Meizu"],
                                [h, A]
                            ],
                            [/(mz)-([\w-]{2,})/i],
                            [
                                [g, "Meizu"], p, [h, A]
                            ],
                            [/android.+a000(1)\s+build/i, /android.+oneplus\s(a\d{4})\s+build/i],
                            [p, [g, "OnePlus"],
                                [h, A]
                            ],
                            [/android.+[;\/]\s*(RCT[\d\w]+)\s+build/i],
                            [p, [g, "RCA"],
                                [h, v]
                            ],
                            [/android.+[;\/\s]+(Venue[\d\s]{2,7})\s+build/i],
                            [p, [g, "Dell"],
                                [h, v]
                            ],
                            [/android.+[;\/]\s*(Q[T|M][\d\w]+)\s+build/i],
                            [p, [g, "Verizon"],
                                [h, v]
                            ],
                            [/android.+[;\/]\s+(Barnes[&\s]+Noble\s+|BN[RT])(V?.*)\s+build/i],
                            [
                                [g, "Barnes & Noble"], p, [h, v]
                            ],
                            [/android.+[;\/]\s+(TM\d{3}.*\b)\s+build/i],
                            [p, [g, "NuVision"],
                                [h, v]
                            ],
                            [/android.+;\s(k88)\sbuild/i],
                            [p, [g, "ZTE"],
                                [h, v]
                            ],
                            [/android.+[;\/]\s*(gen\d{3})\s+build.*49h/i],
                            [p, [g, "Swiss"],
                                [h, A]
                            ],
                            [/android.+[;\/]\s*(zur\d{3})\s+build/i],
                            [p, [g, "Swiss"],
                                [h, v]
                            ],
                            [/android.+[;\/]\s*((Zeki)?TB.*\b)\s+build/i],
                            [p, [g, "Zeki"],
                                [h, v]
                            ],
                            [/(android).+[;\/]\s+([YR]\d{2})\s+build/i, /android.+[;\/]\s+(Dragon[\-\s]+Touch\s+|DT)(\w{5})\sbuild/i],
                            [
                                [g, "Dragon Touch"], p, [h, v]
                            ],
                            [/android.+[;\/]\s*(NS-?\w{0,9})\sbuild/i],
                            [p, [g, "Insignia"],
                                [h, v]
                            ],
                            [/android.+[;\/]\s*((NX|Next)-?\w{0,9})\s+build/i],
                            [p, [g, "NextBook"],
                                [h, v]
                            ],
                            [/android.+[;\/]\s*(Xtreme\_)?(V(1[045]|2[015]|30|40|60|7[05]|90))\s+build/i],
                            [
                                [g, "Voice"], p, [h, A]
                            ],
                            [/android.+[;\/]\s*(LVTEL\-)?(V1[12])\s+build/i],
                            [
                                [g, "LvTel"], p, [h, A]
                            ],
                            [/android.+;\s(PH-1)\s/i],
                            [p, [g, "Essential"],
                                [h, A]
                            ],
                            [/android.+[;\/]\s*(V(100MD|700NA|7011|917G).*\b)\s+build/i],
                            [p, [g, "Envizen"],
                                [h, v]
                            ],
                            [/android.+[;\/]\s*(Le[\s\-]+Pan)[\s\-]+(\w{1,9})\s+build/i],
                            [g, p, [h, v]],
                            [/android.+[;\/]\s*(Trio[\s\-]*.*)\s+build/i],
                            [p, [g, "MachSpeed"],
                                [h, v]
                            ],
                            [/android.+[;\/]\s*(Trinity)[\-\s]*(T\d{3})\s+build/i],
                            [g, p, [h, v]],
                            [/android.+[;\/]\s*TU_(1491)\s+build/i],
                            [p, [g, "Rotor"],
                                [h, v]
                            ],
                            [/android.+(KS(.+))\s+build/i],
                            [p, [g, "Amazon"],
                                [h, v]
                            ],
                            [/android.+(Gigaset)[\s\-]+(Q\w{1,9})\s+build/i],
                            [g, p, [h, v]],
                            [/\s(tablet|tab)[;\/]/i, /\s(mobile)(?:[;\/]|\ssafari)/i],
                            [
                                [h, T.lowerize], g, p
                            ],
                            [/[\s\/\(](smart-?tv)[;\)]/i],
                            [
                                [h, y]
                            ],
                            [/(android[\w\.\s\-]{0,9});.+build/i],
                            [p, [g, "Generic"]]
                        ],
                        engine: [
                            [/windows.+\sedge\/([\w\.]+)/i],
                            [f, [u, "EdgeHTML"]],
                            [/webkit\/537\.36.+chrome\/(?!27)/i],
                            [
                                [u, "Blink"]
                            ],
                            [/(presto)\/([\w\.]+)/i, /(webkit|trident|netfront|netsurf|amaya|lynx|w3m|goanna)\/([\w\.]+)/i, /(khtml|tasman|links)[\/\s]\(?([\w\.]+)/i, /(icab)[\/\s]([23]\.[\d\.]+)/i],
                            [u, f],
                            [/rv\:([\w\.]{1,9}).+(gecko)/i],
                            [f, u]
                        ],
                        os: [
                            [/microsoft\s(windows)\s(vista|xp)/i],
                            [u, f],
                            [/(windows)\snt\s6\.2;\s(arm)/i, /(windows\sphone(?:\sos)*)[\s\/]?([\d\.\s\w]*)/i, /(windows\smobile|windows)[\s\/]?([ntce\d\.\s]+\w)/i],
                            [u, [f, O.str, k.os.windows.version]],
                            [/(win(?=3|9|n)|win\s9x\s)([nt\d\.]+)/i],
                            [
                                [u, "Windows"],
                                [f, O.str, k.os.windows.version]
                            ],
                            [/\((bb)(10);/i],
                            [
                                [u, "BlackBerry"], f
                            ],
                            [/(blackberry)\w*\/?([\w\.]*)/i, /(tizen)[\/\s]([\w\.]+)/i, /(android|webos|palm\sos|qnx|bada|rim\stablet\sos|meego|sailfish|contiki)[\/\s-]?([\w\.]*)/i],
                            [u, f],
                            [/(symbian\s?os|symbos|s60(?=;))[\/\s-]?([\w\.]*)/i],
                            [
                                [u, "Symbian"], f
                            ],
                            [/\((series40);/i],
                            [u],
                            [/mozilla.+\(mobile;.+gecko.+firefox/i],
                            [
                                [u, "Firefox OS"], f
                            ],
                            [/(nintendo|playstation)\s([wids34portablevu]+)/i, /(mint)[\/\s\(]?(\w*)/i, /(mageia|vectorlinux)[;\s]/i, /(joli|[kxln]?ubuntu|debian|suse|opensuse|gentoo|(?=\s)arch|slackware|fedora|mandriva|centos|pclinuxos|redhat|zenwalk|linpus)[\/\s-]?(?!chrom)([\w\.-]*)/i, /(hurd|linux)\s?([\w\.]*)/i, /(gnu)\s?([\w\.]*)/i],
                            [u, f],
                            [/(cros)\s[\w]+\s([\w\.]+\w)/i],
                            [
                                [u, "Chromium OS"], f
                            ],
                            [/(sunos)\s?([\w\.\d]*)/i],
                            [
                                [u, "Solaris"], f
                            ],
                            [/\s([frentopc-]{0,4}bsd|dragonfly)\s?([\w\.]*)/i],
                            [u, f],
                            [/(haiku)\s(\w+)/i],
                            [u, f],
                            [/cfnetwork\/.+darwin/i, /ip[honead]{2,4}(?:.*os\s([\w]+)\slike\smac|;\sopera)/i],
                            [
                                [f, /_/g, "."],
                                [u, "iOS"]
                            ],
                            [/(mac\sos\sx)\s?([\w\s\.]*)/i, /(macintosh|mac(?=_powerpc)\s)/i],
                            [
                                [u, "Mac OS"],
                                [f, /_/g, "."]
                            ],
                            [/((?:open)?solaris)[\/\s-]?([\w\.]*)/i, /(aix)\s((\d)(?=\.|\)|\s)[\w\.])*/i, /(plan\s9|minix|beos|os\/2|amigaos|morphos|risc\sos|openvms|fuchsia)/i, /(unix)\s?([\w\.]*)/i],
                            [u, f]
                        ]
                    },
                    E = function(e, t) {
                        if ("object" == typeof e && (t = e, e = i), !(this instanceof E)) return new E(e, t).getResult();
                        var r = e || (s && s.navigator && s.navigator.userAgent ? s.navigator.userAgent : o),
                            n = t ? T.extend(C, t) : C;
                        return this.getBrowser = function() {
                            var e = {
                                name: i,
                                version: i
                            };
                            return O.rgx.call(e, r, n.browser), e.major = T.major(e.version), e
                        }, this.getCPU = function() {
                            var e = {
                                architecture: i
                            };
                            return O.rgx.call(e, r, n.cpu), e
                        }, this.getDevice = function() {
                            var e = {
                                vendor: i,
                                model: i,
                                type: i
                            };
                            return O.rgx.call(e, r, n.device), e
                        }, this.getEngine = function() {
                            var e = {
                                name: i,
                                version: i
                            };
                            return O.rgx.call(e, r, n.engine), e
                        }, this.getOS = function() {
                            var e = {
                                name: i,
                                version: i
                            };
                            return O.rgx.call(e, r, n.os), e
                        }, this.getResult = function() {
                            return {
                                ua: this.getUA(),
                                browser: this.getBrowser(),
                                engine: this.getEngine(),
                                os: this.getOS(),
                                device: this.getDevice(),
                                cpu: this.getCPU()
                            }
                        }, this.getUA = function() {
                            return r
                        }, this.setUA = function(e) {
                            return r = e, this
                        }, this
                    };
                E.VERSION = "0.7.20", E.BROWSER = {
                    NAME: u,
                    MAJOR: "major",
                    VERSION: f
                }, E.CPU = {
                    ARCHITECTURE: b
                }, E.DEVICE = {
                    MODEL: p,
                    VENDOR: g,
                    TYPE: h,
                    CONSOLE: w,
                    MOBILE: A,
                    SMARTTV: y,
                    TABLET: v,
                    WEARABLE: x,
                    EMBEDDED: "embedded"
                }, E.ENGINE = {
                    NAME: u,
                    VERSION: f
                }, E.OS = {
                    NAME: u,
                    VERSION: f
                }, typeof t !== c ? (typeof e !== c && e.exports && (t = e.exports = E), t.UAParser = E) : 1 ? (n = function() {
                    return E
                }.call(t, r, t, e)) === i || (e.exports = n) : s && (s.UAParser = E);
                var S = s && (s.jQuery || s.Zepto);
                if (typeof S !== c && !S.ua) {
                    var j = new E;
                    S.ua = j.getResult(), S.ua.get = function() {
                        return j.getUA()
                    }, S.ua.set = function(e) {
                        j.setUA(e);
                        var t = j.getResult();
                        for (var r in t) S.ua[r] = t[r]
                    }
                }
            }("object" == typeof window ? window : this)
        },
        99: function(e, t, r) {
            "use strict";
            e.exports = {
                isProduction: "production" == "production",
                isStaging: "production" == "staging",
                isTest: "production" == "test"
            }
        }
    })
});
//# sourceMappingURL=background.js.map