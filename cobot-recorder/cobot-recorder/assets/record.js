var enableCustomPopup = false;
var random=0;
var hash = {};
var popupParams = {
    "Tasks": {
        "accountname": "${arsTasks.accountName}",
        "accountkey": "${arsTasks.accountKey}",
        "comments": "${arsTasks.comments}",
        "endddate": "${arsTasks.endDatecolumn}",
        "endpoint": "${arsTasks.endpoint}",
        "entitlement_valuekey": "${arsTasks.entitlement_valueKey}",
        "userkey": "${arsTasks.users}",
        "securitysystem": "${arsTasks.securitysystem}",
        "source": "${arsTasks.source}",
        "provisioningcomments": "${arsTasks.provisioningComments}",
        "provisioningmetadata": "${arsTasks.provisioningMetadata}",
        "startdate": "${arsTasks.startDatecolumn}",
        "status": "${arsTasks.status}",
        "taskdate": "${arsTasks.taskdate}"
    },
    "Users": {
        "accountName": "${accountName}",
        "username": "${user.username}",
        "systemusername": "${user.systemUserName}",
        "firstname": "${user.firstname}",
        "lastname": "${user.lastname}",
        "email": "${user.email}",
        "city": "${user.city}",
        "comments": "${user.comments}",
        "companyname": "${user.companyname}",
        "costcenter": "${user.costcenter}",
        "country": "${user.country}",
        "createdate": "${user.createdate}",
        "createdby": "${user.createdBy}",
        "departmentname": "${user.departmentname}",
        "departmentnumber": "${user.departmentNumber}",
        "displayname": "${user.displayname}",
        "employeeclass": "${user.employeeclass}",
        "employeeid": "${user.employeeid}",
        "employeetype": "${user.employeeType}",
        "enabled": "${user.enabled}",
        "enddate": "${user.enddate}",
        "entity": "${user.entity}",
        "job_function": "${user.job_function}",
        "jobcode": "${user.jobCode}",
        "jobcodedesc": "${user.jobcodedesc}",
        "jobdescription": "${user.jobDescription}",
        "leavestatus": "${user.leaveStatus}",
        "location": "${user.location}",
        "locationdesc": "${user.locationdesc}",
        "locationnumber": "${user.locationnumber}",
        "manager": "${userManager.displayname}",
        "middlename": "${user.middlename}",
        "orgunitid": "${user.orgunitid}",
        "owner": "${user.owner}",
        "password": "${user.password}",
        "passwordexpired": "${user.passwordExpired}",
        "phonenumber": "${user.phonenumber}",
        "preferedfirstname": "${user.preferedFirstName}",
        "region": "${user.region}",
        "regioncode": "${user.regioncode}",
        "secondaryemail": "${user.secondaryEmail}",
        "secondarymanager": "${user.secondaryManager}",
        "secondaryphone": "${user.secondaryPhone}",
        "siteid": "${user.siteid}",
        "startdate": "${user.startdate}",
        "state": "${user.state}",
        "statuskey": "${user.statuskey}",
        "street": "${user.street}",
        "termdate": "${user.termDate}",
        "title": "${user.title}",
        "customproperty1": "${user.customproperty1}",
        "customproperty2": "${user.customproperty2}",
        "customproperty3": "${user.customproperty3}",
        "customproperty4": "${user.customproperty4}",
        "customproperty5": "${user.customproperty5}",
        "customproperty6": "${user.customproperty6}",
        "customproperty7": "${user.customproperty7}",
        "customproperty8": "${user.customproperty8}",
        "customproperty9": "${user.customproperty9}",
        "customproperty10": "${user.customproperty10}",
        "customproperty11": "${user.customproperty11}",
        "customproperty12": "${user.customproperty12}",
        "customproperty13": "${user.customproperty13}",
        "customproperty14": "${user.customproperty14}",
        "customproperty15": "${user.customproperty15}",
        "customproperty16": "${user.customproperty16}",
        "customproperty17": "${user.customproperty17}",
        "customproperty18": "${user.customproperty18}",
        "customproperty19": "${user.customproperty19}",
        "customproperty20": "${user.customproperty20}",
        "customproperty21": "${user.customproperty21}",
        "customproperty22": "${user.customproperty22}",
        "customproperty23": "${user.customproperty23}",
        "customproperty24": "${user.customproperty24}",
        "customproperty25": "${user.customproperty25}",
        "customproperty26": "${user.customproperty26}",
        "customproperty27": "${user.customproperty27}",
        "customproperty28": "${user.customproperty28}",
        "customproperty29": "${user.customproperty29}",
        "customproperty30": "${user.customproperty30}",
        "customproperty31": "${user.customproperty31}",
        "customproperty32": "${user.customproperty32}",
        "customproperty33": "${user.customproperty33}",
        "customproperty34": "${user.customproperty34}",
        "customproperty35": "${user.customproperty35}",
        "customproperty36": "${user.customproperty36}",
        "customproperty37": "${user.customproperty37}",
        "customproperty38": "${user.customproperty38}",
        "customproperty39": "${user.customproperty39}",
        "customproperty40": "${user.customproperty40}",
        "customproperty41": "${user.customproperty41}",
        "customproperty42": "${user.customproperty42}",
        "customproperty43": "${user.customproperty43}",
        "customproperty44": "${user.customproperty44}",
        "customproperty45": "${user.customproperty45}",
        "customproperty46": "${user.customproperty46}",
        "customproperty47": "${user.customproperty47}",
        "customproperty48": "${user.customproperty48}",
        "customproperty49": "${user.customproperty49}",
        "customproperty50": "${user.customproperty50}",
        "customproperty51": "${user.customproperty51}",
        "customproperty52": "${user.customproperty52}",
        "customproperty53": "${user.customproperty53}",
        "customproperty54": "${user.customproperty54}",
        "customproperty55": "${user.customproperty55}",
        "customproperty56": "${user.customproperty56}",
        "customproperty57": "${user.customproperty57}",
        "customproperty58": "${user.customproperty58}",
        "customproperty59": "${user.customproperty59}",
        "customproperty60": "${user.customproperty60}",
        "customproperty61": "${user.customproperty61}",
        "customproperty62": "${user.customproperty62}",
        "customproperty63": "${user.customproperty63}",
        "customproperty64": "${user.customproperty64}",
        "customproperty65": "${user.customproperty65}"
    },
    "Endpoints": {
        "endpointname": "${endpoints.endpointname}",
        "description": "${endpoints.description}",
        "displayname": "${endpoints.displayName}",
        "applicationurl": "${endpoints.applicationUrl}",
        "customproperty1": "${endpoints.customproperty1}",
        "customproperty2": "${endpoints.customproperty2}",
        "customproperty3": "${endpoints.customproperty3}",
        "customproperty4": "${endpoints.customproperty4}",
        "customproperty5": "${endpoints.customproperty5}",
        "customproperty6": "${endpoints.customproperty6}",
        "customproperty7": "${endpoints.customproperty7}",
        "customproperty8": "${endpoints.customproperty8}",
        "customproperty9": "${endpoints.customproperty9}",
        "customproperty10": "${endpoints.customproperty10}",
        "customproperty11": "${endpoints.customproperty11}",
        "customproperty12": "${endpoints.customproperty12}",
        "customproperty13": "${endpoints.customproperty13}",
        "customproperty14": "${endpoints.customproperty14}",
        "customproperty15": "${endpoints.customproperty15}",
        "customproperty16": "${endpoints.customproperty16}",
        "customproperty17": "${endpoints.customproperty17}",
        "customproperty18": "${endpoints.customproperty18}",
        "customproperty19": "${endpoints.customproperty19}",
       
        "customproperty20": "${endpoints.customproperty20}",
        "customproperty21": "${endpoints.customproperty21}",
        "customproperty22": "${endpoints.customproperty22}",
        "customproperty23": "${endpoints.customproperty23}",
        "customproperty24": "${endpoints.customproperty24}",
        "customproperty25": "${endpoints.customproperty25}",
        "customproperty26": "${endpoints.customproperty26}",
        "customproperty27": "${endpoints.customproperty27}",
        "customproperty28": "${endpoints.customproperty28}",
        "customproperty29": "${endpoints.customproperty29}",
        
        "customproperty30": "${endpoints.customproperty30}",
        "customproperty31": "${endpoints.customproperty31}",
        "customproperty32": "${endpoints.customproperty32}",
        "customproperty33": "${endpoints.customproperty33}",
        "customproperty34": "${endpoints.customproperty34}",
        "customproperty35": "${endpoints.customproperty35}",
        "customproperty36": "${endpoints.customproperty36}",
        "customproperty37": "${endpoints.customproperty37}",
        "customproperty38": "${endpoints.customproperty38}",
        "customproperty39": "${endpoints.customproperty39}",
        
        "customproperty40": "${endpoints.customproperty40}",
        "customproperty41": "${endpoints.customproperty41}",
        "customproperty42": "${endpoints.customproperty42}",
        "customproperty43": "${endpoints.customproperty43}",
        "customproperty44": "${endpoints.customproperty44}",
        "customproperty45": "${endpoints.customproperty45}"
    },
    "Account": {
        "name": "${account.name}",
        "displayname": "${account.displayName}",
        "accountclass": "${account.accountclass}",
        "accountconfig": "${account.accountConfig}",
        "accountid": "${account.accountID}",
        "accounttype": "${account.accounttype}",
        "comments": "${account.comments}",
        "created_on": "${account.createdon}",
        "creator": "${account.creator}",
        "description": "${account.description}",
        "lastlogondate": "${account.lastlogondate}",
        "lastpasswordchange": "${account.lastpasswordchange}",
        "lockedstate": "${account.lockedState}",
        "passwordchangestatus": "${account.passwordchangestatus}",
        "passwordlockdate": "${account.passwordlockdate}",
        "privileged": "${account.privileged}",
        "usergroup": "${account.usergroup}",
        "userlock": "${account.userlock}",
        "validfrom": "${account.validfrom}",
        "validthrough": "${account.validthrough}",
        "customproperty1": "${account.customproperty1}",
        "customproperty2": "${account.customproperty2}",
        "customproperty3": "${account.customproperty3}",
        "customproperty4": "${account.customproperty4}",
        "customproperty5": "${account.customproperty5}",
        "customproperty6": "${account.customproperty6}",
        "customproperty7": "${account.customproperty7}",
        "customproperty8": "${account.customproperty8}",
        "customproperty9": "${account.customproperty9}",
        "customproperty10": "${account.customproperty10}",
        "customproperty11": "${account.customproperty11}",
        "customproperty12": "${account.customproperty12}",
        "customproperty13": "${account.customproperty13}",
        "customproperty14": "${account.customproperty14}",
        "customproperty15": "${account.customproperty15}",
        "customproperty16": "${account.customproperty16}",
        "customproperty17": "${account.customproperty17}",
        "customproperty18": "${account.customproperty18}",
        "customproperty19": "${account.customproperty19}",
        
        "customproperty20": "${account.customproperty20}",
        "customproperty21": "${account.customproperty21}",
        "customproperty22": "${account.customproperty22}",
        "customproperty23": "${account.customproperty23}",
        "customproperty24": "${account.customproperty24}",
        "customproperty25": "${account.customproperty25}",
        "customproperty26": "${account.customproperty26}",
        "customproperty27": "${account.customproperty27}",
        "customproperty28": "${account.customproperty28}",
        "customproperty29": "${account.customproperty29}",
        
        "customproperty30": "${account.customproperty30}",
        "customproperty31": "${account.customproperty31}",
        "customproperty32": "${account.customproperty32}",
        "customproperty33": "${account.customproperty33}",
        "customproperty34": "${account.customproperty34}",
        "customproperty35": "${account.customproperty35}",
        "customproperty36": "${account.customproperty36}",
        "customproperty37": "${account.customproperty37}",
        "customproperty38": "${account.customproperty38}",
        "customproperty39": "${account.customproperty39}",
       
        "customproperty40": "${account.customproperty40}",
        "customproperty41": "${account.customproperty41}",
        "customproperty42": "${account.customproperty42}",
        "customproperty43": "${account.customproperty43}",
        "customproperty44": "${account.customproperty44}",
        "customproperty45": "${account.customproperty45}",
        "customproperty46": "${account.customproperty46}",
        "customproperty47": "${account.customproperty47}",
        "customproperty48": "${account.customproperty48}",
        "customproperty49": "${account.customproperty49}",
        
        "customproperty50": "${account.customproperty50}",
        "customproperty51": "${account.customproperty51}",
        "customproperty52": "${account.customproperty52}",
        "customproperty53": "${account.customproperty53}",
        "customproperty54": "${account.customproperty54}",
        "customproperty55": "${account.customproperty55}",
        "customproperty56": "${account.customproperty56}"
    }
};
! function (e, t) {
    if ("object" == typeof exports && "object" == typeof module) module.exports = t();
    else if ("function" == typeof define && define.amd) define([], t);
    else {
        var n = t();
        for (var i in n) ("object" == typeof exports ? exports : e)[i] = n[i]
    }
}("undefined" != typeof self ? self : this, function () {
    return webpackJsonp([0], {
        100: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.normalizeTestsInSuite = a, t.sanitizeProjectName = s;
            var i, r = (i = n(101)) && i.__esModule ? i : {
                default: i
            };

            function o(e, t, n) {
                return t in e ? Object.defineProperty(e, t, {
                    value: n,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[t] = n, e
            }

            function a({
                suite: e,
                tests: t
            }) {
                if (!e) return;
                let n = function (e) {
                    for (var t = 1; t < arguments.length; t++) {
                        var n = null != arguments[t] ? arguments[t] : {},
                            i = Object.keys(n);
                        "function" == typeof Object.getOwnPropertySymbols && (i = i.concat(Object.getOwnPropertySymbols(n).filter(function (e) {
                            return Object.getOwnPropertyDescriptor(n, e).enumerable
                        }))), i.forEach(function (t) {
                            o(e, t, n[t])
                        })
                    }
                    return e
                }({}, e);
                return n.tests.forEach((e, i) => {
                    n.tests[i] = t.find(t => t.id === e).name
                }), n
            }

            function s(e) {
                let t = e;
                return t.startsWith("http") ? r.default.parse(e).host : t.replace(/([^a-z0-9 ._-]+)/gi, "")
            }
            e.exports = {
                normalizeTestsInSuite: a,
                sanitizeProjectName: s
            }
        },
        101: function (e, t, n) {
            "use strict";
            var i = n(102),
                r = n(103);

            function o() {
                this.protocol = null, this.slashes = null, this.auth = null, this.host = null, this.port = null, this.hostname = null, this.hash = null, this.search = null, this.query = null, this.pathname = null, this.path = null, this.href = null
            }
            t.parse = y, t.resolve = function (e, t) {
                return y(e, !1, !0).resolve(t)
            }, t.resolveObject = function (e, t) {
                return e ? y(e, !1, !0).resolveObject(t) : t
            }, t.format = function (e) {
                r.isString(e) && (e = y(e));
                return e instanceof o ? e.format() : o.prototype.format.call(e)
            }, t.Url = o;
            var a = /^([a-z0-9.+-]+:)/i,
                s = /:[0-9]*$/,
                l = /^(\/\/?(?!\/)[^\?\s]*)(\?[^\s]*)?$/,
                c = ["{", "}", "|", "\\", "^", "`"].concat(["<", ">", '"', "`", " ", "\r", "\n", "\t"]),
                d = ["'"].concat(c),
                u = ["%", "/", "?", ";", "#"].concat(d),
                p = ["/", "?", "#"],
                h = 255,
                m = /^[+a-z0-9A-Z_-]{0,63}$/,
                f = /^([+a-z0-9A-Z_-]{0,63})(.*)$/,
                g = {
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
                v = n(104);

            function y(e, t, n) {
                if (e && r.isObject(e) && e instanceof o) return e;
                var i = new o;
                return i.parse(e, t, n), i
            }
            o.prototype.parse = function (e, t, n) {
                if (!r.isString(e)) throw new TypeError("Parameter 'url' must be a string, not " + typeof e);
                var o = e.indexOf("?"),
                    s = -1 !== o && o < e.indexOf("#") ? "?" : "#",
                    c = e.split(s);
                c[0] = c[0].replace(/\\/g, "/");
                var y = e = c.join(s);
                if (y = y.trim(), !n && 1 === e.split("#").length) {
                    var T = l.exec(y);
                    if (T) return this.path = y, this.href = y, this.pathname = T[1], T[2] ? (this.search = T[2], this.query = t ? v.parse(this.search.substr(1)) : this.search.substr(1)) : t && (this.search = "", this.query = {}), this
                }
                var A = a.exec(y);
                if (A) {
                    var O = (A = A[0]).toLowerCase();
                    this.protocol = O, y = y.substr(A.length)
                }
                if (n || A || y.match(/^\/\/[^@\/]+@[^@\/]+/)) {
                    var x = "//" === y.substr(0, 2);
                    !x || A && b[A] || (y = y.substr(2), this.slashes = !0)
                }
                if (!b[A] && (x || A && !w[A])) {
                    for (var C, S, k = -1, E = 0; E < p.length; E++) {
                        -1 !== (L = y.indexOf(p[E])) && (-1 === k || L < k) && (k = L)
                    } - 1 !== (S = -1 === k ? y.lastIndexOf("@") : y.lastIndexOf("@", k)) && (C = y.slice(0, S), y = y.slice(S + 1), this.auth = decodeURIComponent(C)), k = -1;
                    for (E = 0; E < u.length; E++) {
                        var L; - 1 !== (L = y.indexOf(u[E])) && (-1 === k || L < k) && (k = L)
                    } - 1 === k && (k = y.length), this.host = y.slice(0, k), y = y.slice(k), this.parseHost(), this.hostname = this.hostname || "";
                    var j = "[" === this.hostname[0] && "]" === this.hostname[this.hostname.length - 1];
                    if (!j)
                        for (var R = this.hostname.split(/\./), N = (E = 0, R.length); E < N; E++) {
                            var P = R[E];
                            if (P && !P.match(m)) {
                                for (var M = "", _ = 0, I = P.length; _ < I; _++) P.charCodeAt(_) > 127 ? M += "x" : M += P[_];
                                if (!M.match(m)) {
                                    var D = R.slice(0, E),
                                        V = R.slice(E + 1),
                                        z = P.match(f);
                                    z && (D.push(z[1]), V.unshift(z[2])), V.length && (y = "/" + V.join(".") + y), this.hostname = D.join(".");
                                    break
                                }
                            }
                        }
                    this.hostname.length > h ? this.hostname = "" : this.hostname = this.hostname.toLowerCase(), j || (this.hostname = i.toASCII(this.hostname));
                    var B = this.port ? ":" + this.port : "",
                        U = this.hostname || "";
                    this.host = U + B, this.href += this.host, j && (this.hostname = this.hostname.substr(1, this.hostname.length - 2), "/" !== y[0] && (y = "/" + y))
                }
                if (!g[O])
                    for (E = 0, N = d.length; E < N; E++) {
                        var $ = d[E];
                        if (-1 !== y.indexOf($)) {
                            var H = encodeURIComponent($);
                            H === $ && (H = escape($)), y = y.split($).join(H)
                        }
                    }
                var F = y.indexOf("#"); - 1 !== F && (this.hash = y.substr(F), y = y.slice(0, F));
                var q = y.indexOf("?");
                if (-1 !== q ? (this.search = y.substr(q), this.query = y.substr(q + 1), t && (this.query = v.parse(this.query)), y = y.slice(0, q)) : t && (this.search = "", this.query = {}), y && (this.pathname = y), w[O] && this.hostname && !this.pathname && (this.pathname = "/"), this.pathname || this.search) {
                    B = this.pathname || "";
                    var K = this.search || "";
                    this.path = B + K
                }
                return this.href = this.format(), this
            }, o.prototype.format = function () {
                var e = this.auth || "";
                e && (e = (e = encodeURIComponent(e)).replace(/%3A/i, ":"), e += "@");
                var t = this.protocol || "",
                    n = this.pathname || "",
                    i = this.hash || "",
                    o = !1,
                    a = "";
                this.host ? o = e + this.host : this.hostname && (o = e + (-1 === this.hostname.indexOf(":") ? this.hostname : "[" + this.hostname + "]"), this.port && (o += ":" + this.port)), this.query && r.isObject(this.query) && Object.keys(this.query).length && (a = v.stringify(this.query));
                var s = this.search || a && "?" + a || "";
                return t && ":" !== t.substr(-1) && (t += ":"), this.slashes || (!t || w[t]) && !1 !== o ? (o = "//" + (o || ""), n && "/" !== n.charAt(0) && (n = "/" + n)) : o || (o = ""), i && "#" !== i.charAt(0) && (i = "#" + i), s && "?" !== s.charAt(0) && (s = "?" + s), t + o + (n = n.replace(/[?#]/g, function (e) {
                    return encodeURIComponent(e)
                })) + (s = s.replace("#", "%23")) + i
            }, o.prototype.resolve = function (e) {
                return this.resolveObject(y(e, !1, !0)).format()
            }, o.prototype.resolveObject = function (e) {
                if (r.isString(e)) {
                    var t = new o;
                    t.parse(e, !1, !0), e = t
                }
                for (var n = new o, i = Object.keys(this), a = 0; a < i.length; a++) {
                    var s = i[a];
                    n[s] = this[s]
                }
                if (n.hash = e.hash, "" === e.href) return n.href = n.format(), n;
                if (e.slashes && !e.protocol) {
                    for (var l = Object.keys(e), c = 0; c < l.length; c++) {
                        var d = l[c];
                        "protocol" !== d && (n[d] = e[d])
                    }
                    return w[n.protocol] && n.hostname && !n.pathname && (n.path = n.pathname = "/"), n.href = n.format(), n
                }
                if (e.protocol && e.protocol !== n.protocol) {
                    if (!w[e.protocol]) {
                        for (var u = Object.keys(e), p = 0; p < u.length; p++) {
                            var h = u[p];
                            n[h] = e[h]
                        }
                        return n.href = n.format(), n
                    }
                    if (n.protocol = e.protocol, e.host || b[e.protocol]) n.pathname = e.pathname;
                    else {
                        for (var m = (e.pathname || "").split("/"); m.length && !(e.host = m.shift()););
                        e.host || (e.host = ""), e.hostname || (e.hostname = ""), "" !== m[0] && m.unshift(""), m.length < 2 && m.unshift(""), n.pathname = m.join("/")
                    }
                    if (n.search = e.search, n.query = e.query, n.host = e.host || "", n.auth = e.auth, n.hostname = e.hostname || e.host, n.port = e.port, n.pathname || n.search) {
                        var f = n.pathname || "",
                            g = n.search || "";
                        n.path = f + g
                    }
                    return n.slashes = n.slashes || e.slashes, n.href = n.format(), n
                }
                var v = n.pathname && "/" === n.pathname.charAt(0),
                    y = e.host || e.pathname && "/" === e.pathname.charAt(0),
                    T = y || v || n.host && e.pathname,
                    A = T,
                    O = n.pathname && n.pathname.split("/") || [],
                    x = (m = e.pathname && e.pathname.split("/") || [], n.protocol && !w[n.protocol]);
                if (x && (n.hostname = "", n.port = null, n.host && ("" === O[0] ? O[0] = n.host : O.unshift(n.host)), n.host = "", e.protocol && (e.hostname = null, e.port = null, e.host && ("" === m[0] ? m[0] = e.host : m.unshift(e.host)), e.host = null), T = T && ("" === m[0] || "" === O[0])), y) n.host = e.host || "" === e.host ? e.host : n.host, n.hostname = e.hostname || "" === e.hostname ? e.hostname : n.hostname, n.search = e.search, n.query = e.query, O = m;
                else if (m.length) O || (O = []), O.pop(), O = O.concat(m), n.search = e.search, n.query = e.query;
                else if (!r.isNullOrUndefined(e.search)) {
                    if (x) n.hostname = n.host = O.shift(), (L = !!(n.host && n.host.indexOf("@") > 0) && n.host.split("@")) && (n.auth = L.shift(), n.host = n.hostname = L.shift());
                    return n.search = e.search, n.query = e.query, r.isNull(n.pathname) && r.isNull(n.search) || (n.path = (n.pathname ? n.pathname : "") + (n.search ? n.search : "")), n.href = n.format(), n
                }
                if (!O.length) return n.pathname = null, n.search ? n.path = "/" + n.search : n.path = null, n.href = n.format(), n;
                for (var C = O.slice(-1)[0], S = (n.host || e.host || O.length > 1) && ("." === C || ".." === C) || "" === C, k = 0, E = O.length; E >= 0; E--) "." === (C = O[E]) ? O.splice(E, 1) : ".." === C ? (O.splice(E, 1), k++) : k && (O.splice(E, 1), k--);
                if (!T && !A)
                    for (; k--; k) O.unshift("..");
                !T || "" === O[0] || O[0] && "/" === O[0].charAt(0) || O.unshift(""), S && "/" !== O.join("/").substr(-1) && O.push("");
                var L, j = "" === O[0] || O[0] && "/" === O[0].charAt(0);
                x && (n.hostname = n.host = j ? "" : O.length ? O.shift() : "", (L = !!(n.host && n.host.indexOf("@") > 0) && n.host.split("@")) && (n.auth = L.shift(), n.host = n.hostname = L.shift()));
                return (T = T || n.host && O.length) && !j && O.unshift(""), O.length ? n.pathname = O.join("/") : (n.pathname = null, n.path = null), r.isNull(n.pathname) && r.isNull(n.search) || (n.path = (n.pathname ? n.pathname : "") + (n.search ? n.search : "")), n.auth = e.auth || n.auth, n.slashes = n.slashes || e.slashes, n.href = n.format(), n
            }, o.prototype.parseHost = function () {
                var e = this.host,
                    t = s.exec(e);
                t && (":" !== (t = t[0]) && (this.port = t.substr(1)), e = e.substr(0, e.length - t.length)), e && (this.hostname = e)
            }
        },
        102: function (e, t, n) {
            (function (e, i) {
                var r;
                ! function (o) {
                    var a = "object" == typeof t && t && !t.nodeType && t,
                        s = "object" == typeof e && e && !e.nodeType && e,
                        l = "object" == typeof i && i;
                    l.global !== l && l.window !== l && l.self !== l || (o = l);
                    var c, d, u = 2147483647,
                        p = 36,
                        h = 1,
                        m = 26,
                        f = 38,
                        g = 700,
                        b = 72,
                        w = 128,
                        v = "-",
                        y = /^xn--/,
                        T = /[^\x20-\x7E]/,
                        A = /[\x2E\u3002\uFF0E\uFF61]/g,
                        O = {
                            overflow: "Overflow: input needs wider integers to process",
                            "not-basic": "Illegal input >= 0x80 (not a basic code point)",
                            "invalid-input": "Invalid input"
                        },
                        x = p - h,
                        C = Math.floor,
                        S = String.fromCharCode;

                    function k(e) {
                        throw new RangeError(O[e])
                    }

                    function E(e, t) {
                        for (var n = e.length, i = []; n--;) i[n] = t(e[n]);
                        return i
                    }

                    function L(e, t) {
                        var n = e.split("@"),
                            i = "";
                        return n.length > 1 && (i = n[0] + "@", e = n[1]), i + E((e = e.replace(A, ".")).split("."), t).join(".")
                    }

                    function j(e) {
                        for (var t, n, i = [], r = 0, o = e.length; r < o;)(t = e.charCodeAt(r++)) >= 55296 && t <= 56319 && r < o ? 56320 == (64512 & (n = e.charCodeAt(r++))) ? i.push(((1023 & t) << 10) + (1023 & n) + 65536) : (i.push(t), r--) : i.push(t);
                        return i
                    }

                    function R(e) {
                        return E(e, function (e) {
                            var t = "";
                            return e > 65535 && (t += S((e -= 65536) >>> 10 & 1023 | 55296), e = 56320 | 1023 & e), t += S(e)
                        }).join("")
                    }

                    function N(e, t) {
                        return e + 22 + 75 * (e < 26) - ((0 != t) << 5)
                    }

                    function P(e, t, n) {
                        var i = 0;
                        for (e = n ? C(e / g) : e >> 1, e += C(e / t); e > x * m >> 1; i += p) e = C(e / x);
                        return C(i + (x + 1) * e / (e + f))
                    }

                    function M(e) {
                        var t, n, i, r, o, a, s, l, c, d, f, g = [],
                            y = e.length,
                            T = 0,
                            A = w,
                            O = b;
                        for ((n = e.lastIndexOf(v)) < 0 && (n = 0), i = 0; i < n; ++i) e.charCodeAt(i) >= 128 && k("not-basic"), g.push(e.charCodeAt(i));
                        for (r = n > 0 ? n + 1 : 0; r < y;) {
                            for (o = T, a = 1, s = p; r >= y && k("invalid-input"), ((l = (f = e.charCodeAt(r++)) - 48 < 10 ? f - 22 : f - 65 < 26 ? f - 65 : f - 97 < 26 ? f - 97 : p) >= p || l > C((u - T) / a)) && k("overflow"), T += l * a, !(l < (c = s <= O ? h : s >= O + m ? m : s - O)); s += p) a > C(u / (d = p - c)) && k("overflow"), a *= d;
                            O = P(T - o, t = g.length + 1, 0 == o), C(T / t) > u - A && k("overflow"), A += C(T / t), T %= t, g.splice(T++, 0, A)
                        }
                        return R(g)
                    }

                    function _(e) {
                        var t, n, i, r, o, a, s, l, c, d, f, g, y, T, A, O = [];
                        for (g = (e = j(e)).length, t = w, n = 0, o = b, a = 0; a < g; ++a)(f = e[a]) < 128 && O.push(S(f));
                        for (i = r = O.length, r && O.push(v); i < g;) {
                            for (s = u, a = 0; a < g; ++a)(f = e[a]) >= t && f < s && (s = f);
                            for (s - t > C((u - n) / (y = i + 1)) && k("overflow"), n += (s - t) * y, t = s, a = 0; a < g; ++a)
                                if ((f = e[a]) < t && ++n > u && k("overflow"), f == t) {
                                    for (l = n, c = p; !(l < (d = c <= o ? h : c >= o + m ? m : c - o)); c += p) A = l - d, T = p - d, O.push(S(N(d + A % T, 0))), l = C(A / T);
                                    O.push(S(N(l, 0))), o = P(n, y, i == r), n = 0, ++i
                                } ++n, ++t
                        }
                        return O.join("")
                    }
                    if (c = {
                        version: "1.4.1",
                        ucs2: {
                            decode: j,
                            encode: R
                        },
                        decode: M,
                        encode: _,
                        toASCII: function (e) {
                            return L(e, function (e) {
                                return T.test(e) ? "xn--" + _(e) : e
                            })
                        },
                        toUnicode: function (e) {
                            return L(e, function (e) {
                                return y.test(e) ? M(e.slice(4).toLowerCase()) : e
                            })
                        }
                    }, 1) void 0 === (r = function () {
                        return c
                    }.call(t, n, t, e)) || (e.exports = r);
                    else if (a && s)
                        if (e.exports == a) s.exports = c;
                        else
                            for (d in c) c.hasOwnProperty(d) && (a[d] = c[d]);
                    else o.punycode = c
                }(this)
            }).call(t, n(58)(e), n(8))
        },
        103: function (e, t, n) {
            "use strict";
            e.exports = {
                isString: function (e) {
                    return "string" == typeof e
                },
                isObject: function (e) {
                    return "object" == typeof e && null !== e
                },
                isNull: function (e) {
                    return null === e
                },
                isNullOrUndefined: function (e) {
                    return null == e
                }
            }
        },
        104: function (e, t, n) {
            "use strict";
            t.decode = t.parse = n(105), t.encode = t.stringify = n(106)
        },
        105: function (e, t, n) {
            "use strict";

            function i(e, t) {
                return Object.prototype.hasOwnProperty.call(e, t)
            }
            e.exports = function (e, t, n, o) {
                t = t || "&", n = n || "=";
                var a = {};
                if ("string" != typeof e || 0 === e.length) return a;
                var s = /\+/g;
                e = e.split(t);
                var l = 1e3;
                o && "number" == typeof o.maxKeys && (l = o.maxKeys);
                var c = e.length;
                l > 0 && c > l && (c = l);
                for (var d = 0; d < c; ++d) {
                    var u, p, h, m, f = e[d].replace(s, "%20"),
                        g = f.indexOf(n);
                    g >= 0 ? (u = f.substr(0, g), p = f.substr(g + 1)) : (u = f, p = ""), h = decodeURIComponent(u), m = decodeURIComponent(p), i(a, h) ? r(a[h]) ? a[h].push(m) : a[h] = [a[h], m] : a[h] = m
                }
                return a
            };
            var r = Array.isArray || function (e) {
                return "[object Array]" === Object.prototype.toString.call(e)
            }
        },
        106: function (e, t, n) {
            "use strict";
            var i = function (e) {
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
            e.exports = function (e, t, n, s) {
                return t = t || "&", n = n || "=", null === e && (e = void 0), "object" == typeof e ? o(a(e), function (a) {
                    var s = encodeURIComponent(i(a)) + n;
                    return r(e[a]) ? o(e[a], function (e) {
                        return s + encodeURIComponent(i(e))
                    }).join(t) : s + encodeURIComponent(i(e[a]))
                }).join(t) : s ? encodeURIComponent(i(s)) + n + encodeURIComponent(i(e)) : ""
            };
            var r = Array.isArray || function (e) {
                return "[object Array]" === Object.prototype.toString.call(e)
            };

            function o(e, t) {
                if (e.map) return e.map(t);
                for (var n = [], i = 0; i < e.length; i++) n.push(t(e[i], i));
                return n
            }
            var a = Object.keys || function (e) {
                var t = [];
                for (var n in e) Object.prototype.hasOwnProperty.call(e, n) && t.push(n);
                return t
            }
        },
        36: function (e, t, n) {
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
        420: function (e, t, n) {
            e.exports = n(421)
        },
        421: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            });
            var i = n(15),
                r = n.n(i),
                o = n(90),
                a = n(422),
                s = n(423),
                l = n(257),
                c = n(45);
            n.d(t, "record", function () {
                return a["b"]
            });
            const d = new l["a"](window);
            let u, p, h, m, f, g, b, w;
            t["locatorBuilders"] = d, Object(s["a"])(a["b"]), a["a"].inputTypes = ["text", "password", "file", "datetime", "datetime-local", "date", "month", "time", "week", "number", "range", "email", "url", "search", "tel", "color"], a["a"].addEventHandler("type", "change", function (e) {
                if (e.target.tagName && !this.recordingState.preventType && 0 == this.recordingState.typeLock && (this.recordingState.typeLock = 1)) {
                    let t = e.target.tagName.toLowerCase(),
                        n = e.target.type;
                    if ("input" == t && a["a"].inputTypes.indexOf(n) >= 0)
                        if (e.target.value.length > 0) {
                            if (Object(a["b"])("type", d.buildAll(e.target), e.target.value), null != this.recordingState.enterTarget) {
                                let t = e.target.parentElement,
                                    n = t.tagName.toLowerCase();
                                for (;
                                    "form" != n && "body" != n;) n = (t = t.parentElement).tagName.toLowerCase();
                                Object(a["b"])("sendKeys", d.buildAll(this.recordingState.enterTarget), "${KEY_ENTER}"), this.recordingState.enterTarget = null
                            }
                        } else Object(a["b"])("type", d.buildAll(e.target), e.target.value);
                    else "textarea" == t && Object(a["b"])("type", d.buildAll(e.target), e.target.value)
                }
                this.recordingState.typeLock = 0
            }), a["a"].addEventHandler("type", "input", function (e) {
                this.recordingState.typeTarget = e.target
            }), a["a"].addEventHandler("clickAt", "click", function (e) {
                0 == e.button && !this.recordingState.preventClick && function (e) {
                    return !!c["c"] || e.isTrusted
                }(e) && (this.recordingState.preventClickTwice || (Object(a["b"])("click", d.buildAll(e.target), ""), this.recordingState.preventClickTwice = !0), setTimeout(() => {
                    this.recordingState.preventClickTwice = !1
                }, 30))
            }, !0), a["a"].addEventHandler("doubleClickAt", "dblclick", function (e) {
                Object(a["b"])("doubleClick", d.buildAll(e.target), "")
            }, !0), a["a"].addEventHandler("sendKeys", "keydown", function (e) {
                console.log(e.target.tagName);
                if (e.target.tagName) {
                    let t = e.keyCode,
                        n = e.target.tagName.toLowerCase(),
                        i = e.target.type;
                    console.log(t);
                    console.log(n);
                    console.log(i);
                    if ("input" == n && a["a"].inputTypes.indexOf(i) >= 0) {
                        if (13 == t) {
                            this.recordingState.enterTarget = e.target, this.recordingState.enterValue = this.recordingState.enterTarget.value;
                            console.log(this.recordingState.enterTarget);
                            console.log(this.recordingState.enterValue);

                            let t = e.target.parentElement,
                                r = t.tagName.toLowerCase();
                            console.log(t);
                            console.log(r);
                            if (this.recordingState.tempValue == this.recordingState.enterTarget.value && this.recordingState.tabCheck == this.recordingState.enterTarget) Object(a["b"])("sendKeys", d.buildAll(this.recordingState.enterTarget), "${KEY_ENTER}"), this.recordingState.enterTarget = null, this.recordingState.preventType = !0;
                            else if (this.recordingState.focusValue == this.recordingState.enterValue) {
                                for (;
                                    "form" != r && "body" != r;) r = (t = t.parentElement).tagName.toLowerCase();
                                Object(a["b"])("sendKeys", d.buildAll(this.recordingState.enterTarget), "${KEY_ENTER}"), this.recordingState.enterTarget = null

                            }
                            if (this.recordingState.typeTarget && this.recordingState.typeTarget.tagName && !this.recordingState.preventType && (this.recordingState.typeLock = 1))
                                if (n = this.recordingState.typeTarget.tagName.toLowerCase(), i = this.recordingState.typeTarget.type, "input" == n && a["a"].inputTypes.indexOf(i) >= 0)
                                    if (this.recordingState.typeTarget.value.length > 0) {
                                        if (Object(a["b"])("type", d.buildAll(this.recordingState.typeTarget), this.recordingState.typeTarget.value), null != this.recordingState.enterTarget) {
                                            for (r = (t = this.recordingState.typeTarget.parentElement).tagName.toLowerCase();
                                                "form" != r && "body" != r;) r = (t = t.parentElement).tagName.toLowerCase();
                                            Object(a["b"])("sendKeys", d.buildAll(this.recordingState.enterTarget), "${KEY_ENTER}"), this.recordingState.enterTarget = null
                                        }
                                    } else Object(a["b"])("type", d.buildAll(this.recordingState.typeTarget), this.recordingState.typeTarget.value);
                                else "textarea" == n && Object(a["b"])("type", d.buildAll(this.recordingState.typeTarget), this.recordingState.typeTarget.value);
                            this.recordingState.preventClick = !0, setTimeout(() => {
                                this.recordingState.preventClick = !1
                            }, 500), setTimeout(() => {
                                this.recordingState.enterValue != e.target.value && (this.recordingState.enterTarget = null)
                            }, 50)
                        }
                        let r = !1;
                        38 != t && 40 != t || "" == e.target.value || (null != this.recordingState.focusTarget && this.recordingState.focusTarget.value != this.recordingState.tempValue && (r = !0, this.recordingState.tempValue = this.recordingState.focusTarget.value), r && Object(a["b"])("type", d.buildAll(e.target), this.recordingState.tempValue), setTimeout(() => {
                            this.recordingState.tempValue = this.recordingState.focusTarget.value
                        }, 250), 38 == t ? Object(a["b"])("sendKeys", d.buildAll(e.target), "${KEY_UP}") : Object(a["b"])("sendKeys", d.buildAll(e.target), "${KEY_DOWN}"), this.recordingState.tabCheck = e.target), 9 == t && this.recordingState.tabCheck == e.target && (Object(a["b"])("sendKeys", d.buildAll(e.target), "${KEY_TAB}"), this.recordingState.preventType = !0)
                    }
                }
            }, !0), a["a"].addEventHandler("dragAndDrop", "mousedown", function (e) {
                if (e.clientX < window.document.documentElement.clientWidth && e.clientY < window.document.documentElement.clientHeight && (u = e, p = setTimeout(() => {
                    u = void 0
                }, 200), h = setTimeout(() => {
                    m = e
                }, 200)), f = [], e.target.nodeName) {
                    if ("option" == e.target.nodeName.toLowerCase()) {
                        let t = e.target.parentNode;
                        if (t.multiple) {
                            let e = t.options;
                            for (let t = 0; t < e.length; t++) e[t]._wasSelected = e[t].selected
                        }
                    }
                }
            }, !0), a["a"].addEventHandler("dragAndDrop", "mouseup", function (e) {
                if (clearTimeout(h), m) {
                    let t = e.clientX - m.clientX,
                        n = e.clientY - m.clientY;
                    if (m && 0 === e.button && t + n && e.clientX < window.document.documentElement.clientWidth && e.clientY < window.document.documentElement.clientHeight && "" === function () {
                        let e = "",
                            t = window.document.activeElement,
                            n = t ? t.tagName.toLowerCase() : null;
                        return "textarea" == n || "input" == n ? e = t.value.slice(t.selectionStart, t.selectionEnd) : window.getSelection && (e = window.getSelection().toString()), e.trim()
                    }()) {
                        let t, n, i = m.pageX - m.target.getBoundingClientRect().left - window.scrollX,
                            r = m.pageY - m.target.getBoundingClientRect().top - window.scrollY;
                        f.length && f[1].relatedTarget == f[0].target && f[0].target == e.target ? (t = e.pageX - f[1].target.getBoundingClientRect().left - window.scrollX, n = e.pageY - f[1].target.getBoundingClientRect().top - window.scrollY, Object(a["b"])("mouseDownAt", d.buildAll(m.target), i + "," + r), Object(a["b"])("mouseMoveAt", d.buildAll(f[1].target), t + "," + n), Object(a["b"])("mouseUpAt", d.buildAll(f[1].target), t + "," + n)) : (t = e.pageX - e.target.getBoundingClientRect().left - window.scrollX, n = e.pageY - e.target.getBoundingClientRect().top - window.scrollY, Object(a["b"])("mouseDownAt", d.buildAll(e.target), t + "," + n), Object(a["b"])("mouseMoveAt", d.buildAll(e.target), t + "," + n), Object(a["b"])("mouseUpAt", d.buildAll(e.target), t + "," + n))
                    }
                } else {
                    g = void 0, p = void 0;
                    let t = e.clientX - u.clientX,
                        n = e.clientY - u.clientY;
                    if (!u || u.target === e.target || t + n) {
                        if (u && u.target === e.target) {
                            d.buildAll(u.target)
                        }
                    } else Object(a["b"])("mouseDown", d.buildAll(u.target), ""), Object(a["b"])("mouseUp", d.buildAll(e.target), "")
                }
                u = void 0, m = void 0, f = void 0
            }, !0), a["a"].addEventHandler("dragAndDropToObject", "dragstart", function (e) {
                b = setTimeout(() => {
                    w = e
                }, 200)
            }, !0), a["a"].addEventHandler("dragAndDropToObject", "drop", function (e) {
                clearTimeout(b), w && 0 == e.button && w.target !== e.target && Object(a["b"])("dragAndDropToObject", d.buildAll(w.target), d.build(e.target)), w = void 0, m = void 0
            }, !0);
            let v, y = null;
            a["a"].addEventHandler("runScript", "scroll", function (e) {
                !0 === E && (v = e.target, clearTimeout(y), y = setTimeout(() => {
                    v = void 0
                }, 500))
            }, !0);
            let T, A, O, x = 0;
            a["a"].addEventHandler("mouseOver", "mouseover", function (e) {
                if (window.document.documentElement && (x = window.document.documentElement.getElementsByTagName("*").length), !0 === E) {
                    (function e(t) {
                        if (!t.tagName) return null;
                        let n = t.tagName.toLowerCase();
                        let i = t.type;
                        return t.hasAttribute("onclick") || t.hasAttribute("href") || "button" == n || "input" == n && ("submit" == i || "button" == i || "image" == i || "radio" == i || "checkbox" == i || "reset" == i) ? t : null != t.parentNode ? e(t.parentNode) : null
                    })(e.target) && (A = e.target, O = d.buildAll(e.target), setTimeout(() => {
                        A = void 0, O = void 0
                    }, 500)), f && (f.length >= 3 && f.shift(), f.push(e))
                }
            }, !0);
            let C = void 0;
            a["a"].addEventHandler("mouseOut", "mouseout", function (e) {
                null !== C && e.target === C && Object(a["b"])("mouseOut", d.buildAll(e.target), ""), C = void 0
            }, !0), a["a"].addMutationObserver("FrameDeleted", function (e) {
                e.forEach(async e => {
                    const t = await e.removedNodes;
                    t.length && "IFRAME" === t[0].nodeName && "selenium-ide-indicator" !== t[0].id && r.a.runtime.sendMessage({
                        frameRemoved: !0
                    }).catch(() => { })
                })
            }, {
                childList: !0
            }), a["a"].addMutationObserver("DOMNodeInserted", function (e) {
                if (!0 === E && window.document.documentElement.getElementsByTagName("*").length > x) {
                    if (!e.reduce((e, t) => ("childList" === t.type && e.push.apply(e, t.addedNodes), e), []).length) return;
                    v && (Object(a["b"])("runScript", [
                        ["window.scrollTo(0," + window.scrollY + ")"]
                    ], ""), E = !1, setTimeout(() => {
                        E = !0
                    }, 550), v = void 0, A = void 0), A && (Object(a["b"])("mouseOver", O, ""), C = A, A = void 0, O = void 0, T = void 0)
                }
            }, {
                childList: !0,
                subtree: !0
            });
            let S, k = null,
                E = !0;
            a["a"].addEventHandler("checkPageLoaded", "readystatechange", function (e) {
                "loading" === window.document.readyState ? E = !1 : (E = !1, clearTimeout(k), k = setTimeout(() => {
                    E = !0
                }, 1500))
            }, !0), a["a"].addEventHandler("contextMenu", "contextmenu", function (e) {
                let t = r.a.runtime.connect(),
                    n = d.buildAll(e.target),
                    i = o["a"].dom.getVisibleText(e.target),
                    s = o["c"].string.normalizeSpaces(e.target.ownerDocument.title);
                t.onMessage.addListener(function (r) {
                    r.cmd.includes("Text") ? Object(a["b"])(r.cmd, n, i) : r.cmd.includes("Title") ? Object(a["b"])(r.cmd, [
                        [s]
                    ], "") : r.cmd.includes("Value") ? Object(a["b"])(r.cmd, n, e.target.value) : "mouseOver" === r.cmd && Object(a["b"])("mouseOver", d.buildAll(e.target), ""), t.onMessage.removeListener(this)
                })
            }, !0);
            let L, j = 0;

            function R(e) {
                let t = e.text.replace(/^ *(.*?) *$/, "$1");
                return t.match(/\xA0/) ? "label=regexp:" + t.replace(/[(\)\[\]\\\^\$\*\+\?\.\|\{\}]/g, function (e) {
                    return "\\" + e
                }).replace(/\s+/g, function (e) {
                    return e.match(/\xA0/) ? e.length > 1 ? "\\s+" : "\\s" : e
                }) : "label=" + t
            }
            a["a"].addEventHandler("editContent", "focus", function (e) {
                "true" == e.target.contentEditable && (S = e.target, L = S.innerHTML, j = 1)
            }, !0), a["a"].addEventHandler("editContent", "blur", function (e) {
                1 == j && e.target == S && (S.innerHTML != L && Object(a["b"])("editContent", d.buildAll(e.target), S.innerHTML), j = 0)
            }, !0), r.a.runtime.sendMessage({
                attachRecorderRequest: !0
            }).catch(function (e) { }), a["a"].prototype.getOptionLocator = function (e) {
                let t = e.text.replace(/^ *(.*?) *$/, "$1");
                return t.match(/\xA0/) ? "label=regexp:" + t.replace(/[\(\)\[\]\\\^\$\*\+\?\.\|\{\}]/g, function (e) {
                    return "\\" + e
                }).replace(/\s+/g, function (e) {
                    return e.match(/\xA0/) ? e.length > 1 ? "\\s+" : "\\s" : e
                }) : "label=" + t
            }, a["a"].addEventHandler("select", "focus", function (e) {
                if (e.target.nodeName) {
                    if ("select" == e.target.nodeName.toLowerCase() && e.target.multiple) {
                        let t = e.target.options;
                        for (let e = 0; e < t.length; e++) null == t[e]._wasSelected && (t[e]._wasSelected = t[e].selected)
                    }
                }
            }, !0), a["a"].addEventHandler("select", "change", function (e) {
                if (e.target.tagName) {
                    if ("select" == e.target.tagName.toLowerCase())
                        if (e.target.multiple) {
                            let t = e.target.options;
                            for (let n = 0; n < t.length; n++)
                                if (t[n]._wasSelected != t[n].selected) {
                                    let i = R(t[n]);
                                    t[n].selected ? Object(a["b"])("addSelection", d.buildAll(e.target), i) : Object(a["b"])("removeSelection", d.buildAll(e.target), i), this.recordingState.preventClickTwice = !0, t[n]._wasSelected = t[n].selected
                                }
                        } else {
                            let t = e.target.options[e.target.selectedIndex];
                            Object(a["b"])("select", d.buildAll(e.target), R(t))
                        }
                }
            }), r.a.runtime.sendMessage({
                attachRecorderRequest: !0
            }).then(e => {
                e && a["c"].attach()
            }).catch(() => { })
        },
        422: function (e, t, n) {
            "use strict";
            t["b"] = b, n.d(t, "a", function () {
                return c
            }), n.d(t, "c", function () {
                return h
            });
            var i = n(15),
                r = n.n(i),
                o = n(130);
            let a, s = -1,
                l = "";

            function c(e) {
                this.window = e, this.eventListeners = {}, this.attached = !1, this.recordingState = {}
            }

            function d(e) {
                let t = window.document.getElementsByTagName("input");
                for (let n = 0; n < t.length; n++) c.inputTypes.indexOf(t[n].type) >= 0 && e(t[n])
            }

            function u(e, t) {
                e.focusTarget = t.target, e.focusValue = e.focusTarget.value, e.tempValue = e.focusValue, e.preventType = !1
            }

            function p(e) {
                e.focusTarget = null, e.focusValue = null, e.tempValue = null
            }
            c.eventHandlers = {}, c.mutationObservers = {}, c.addEventHandler = function (e, t, n, i) {
                n.handlerName = e, i || (i = !1);
                let r = i ? "C_" + t : t;
                this.eventHandlers[r] || (this.eventHandlers[r] = []), this.eventHandlers[r].push(n)
            }, c.addMutationObserver = function (e, t, n) {
                const i = new MutationObserver(t);
                i.observerName = e, i.config = n, this.mutationObservers[e] = i
            }, c.prototype.parseEventKey = function (e) {
                return e.match(/^C_/) ? {
                    eventName: e.substring(2),
                    capture: !0
                } : {
                        eventName: e,
                        capture: !1
                    }
            }, c.prototype.attach = function () {
                if (!this.attached) {
                    for (let e in c.eventHandlers) {
                        const t = this.parseEventKey(e),
                            n = t.eventName,
                            i = t.capture,
                            r = c.eventHandlers[e];
                        this.eventListeners[e] = [];
                        for (let t = 0; t < r.length; t++) this.window.document.addEventListener(n, r[t].bind(this), i), this.eventListeners[e].push(r[t])
                    }
                    for (let e in c.mutationObservers) {
                        const t = c.mutationObservers[e];
                        t.observe(this.window.document.body, t.config)
                    }
                    this.attached = !0, this.recordingState = {
                        typeTarget: void 0,
                        typeLock: 0,
                        focusTarget: null,
                        focusValue: null,
                        tempValue: null,
                        preventType: !1,
                        preventClickTwice: !1,
                        preventClick: !1,
                        enterTarget: null,
                        enterValue: null,
                        tabCheck: null
                    }, e = this.recordingState, d(t => {
                        t.addEventListener("focus", u.bind(null, e)), t.addEventListener("blur", p.bind(null, e))
                    }), m()
                }
                var e
            }, c.prototype.detach = function () {
                for (let e in this.eventListeners) {
                    const t = this.parseEventKey(e),
                        n = t.eventName,
                        i = t.capture;
                    for (let t = 0; t < this.eventListeners[e].length; t++) this.window.document.removeEventListener(n, this.eventListeners[e][t], i)
                }
                for (let e in c.mutationObservers) {
                    c.mutationObservers[e].disconnect()
                }
                var e;
                this.eventListeners = {}, this.attached = !1, f(), e = this.recordingState, d(t => {
                    t.removeEventListener("focus", u.bind(null, e)), t.removeEventListener("blur", p.bind(null, e))
                })
            };
            const h = new c(window);

            function m() {
                if ("root" === l && !a) {
                    const e = window.parent.frames.length;
                    return (a = window.document.createElement("iframe")).src = r.a.runtime.getURL("/indicator.html"), a.id = "selenium-ide-indicator", a.style.border = "1px solid #d30100", a.style.borderRadius = "50px", a.style.position = "fixed", a.style.bottom = "36px", a.style.right = "36px", a.style.width = "400px", a.style.height = "50px", a.style["background-color"] = "#f7f7f7", a.style["box-shadow"] = "0 7px 10px 0 rgba(0,0,0,0.1)", a.style.transition = "bottom 100ms linear", a.style["z-index"] = 1e15,
                        a.addEventListener("mouseenter", function (e) {
                            // e.target.style.visibility = "hidden", setTimeout(function () {
                            //     e.target.style.visibility = "visible"
                            // }, 1e3)
                        }, !1), window.document.body.appendChild(a),
                        (b = window.document.createElement("BUTTON")),
                        i = window.document.createElement("i"),
                        // i.setAttribute("class","fa fa-text-width fa-2x"),
                        i.style.color = "#FFF",
                        b.appendChild(i),
                        b.id = "text_validator_cobot_web_div",
                        b.innerHTML = "VALIDATE TEXT",
                        b.setAttribute("title", "Text Validator"),
                        //b.style.border = "1px solid #000", 
                        //b.style.borderRadius = "50px", 
                        b.style.position = "fixed",
                        b.style.bottom = "36px",
                        b.style.right = "36px",
                        //b.style.width = "50px", 
                        //b.style.height = "50px", 
                        //b.style.top = "500px",
                        // b.style["background-color"] = "#f7f7f7", 
                        b.style["box-shadow"] = "0 7px 10px 0 rgba(0,0,0,0.1)",
                        b.style.transition = "bottom 100ms linear",
                        b.style["z-index"] = 99999998,
                        //b.style.cursor = "move",
                        b.style.color = "#fff",
                        // b.style.position = "absolute", 
                        b.style.bottom = "36px",
                        b.style.left = "36px",
                        // b.style.top = "500px",
                        //  b.style.width = "120px", 
                        b.style.height = "50px",
                        b.style["background-color"] = "#2196F3",

                        // b.style["z-index"] = 1e15, 
                        b.addEventListener("click", function (e) {

                            cobotPopup("validate", e, null, r);
                            //                       var iDiv = document.createElement('div');
                            //     iDiv.id = 'textvalidator_cobot_overlay';
                            //     iDiv.style.position = "fixed";
                            //     iDiv.style.height = "100%";
                            //     iDiv.style.width = "100%";
                            //     iDiv.style.top = "0";
                            //     iDiv.style.right = "0";
                            //     iDiv.style.bottom = "0";
                            //     iDiv.style.left = "0";
                            //     iDiv.style.zIndex = "10000";
                            //     iDiv.style.background = "rgba(0,0,0,0.8)";
                            //     iDiv.style.display = "none";

                            //     var _iDiv = document.createElement('div');
                            //     _iDiv.id = 'textvalidator_popup';
                            //     _iDiv.style.maxWidth  = "400px";
                            //     _iDiv.style.width = "80%";
                            //     _iDiv.style.maxHeight = "190px";
                            //     _iDiv.style.height = "80%";
                            //     _iDiv.style.padding = "10px";
                            //     _iDiv.style.position = "relative";
                            //     _iDiv.style.background = "#fff";
                            //     _iDiv.style.margin = "0px auto";

                            //     var __iDiv = document.createElement('div');
                            //     var t2 = document.createElement("p");
                            //     t2.innerHTML = "X";
                            //     t2.style.position ="absolute";
                            //     t2.style.right ="11px";
                            //     t2.style.top ="5px";
                            //      __iDiv.appendChild(t2);
                            //     __iDiv.id = 'textvalidator_close';
                            //     _iDiv.style.position = "absolute";
                            //     _iDiv.style.top = "10px";
                            //    // _iDiv.style.right = "10px";
                            //     _iDiv.style.cursor = "pointer";
                            //     _iDiv.style.color = "#000";
                            //     __iDiv.onclick = function () {
                            //         popupClose("textvalidator_cobot_overlay");
                            //         window.document.body.removeChild(iDiv)
                            //     };
                            //     var h = document.createElement("H1");
                            //     var t = document.createElement("p");
                            //     t.innerHTML = "Please Enter Text For Validate";
                            //     t.style.fontSize = "16px";
                            //     h.appendChild(t);
                            //     var p = document.createElement("p");
                            //     p.appendChild(t);
                            //     _iDiv.appendChild(__iDiv);
                            //     // _iDiv.appendChild(h);
                            //     var x = document.createElement("textarea");
                            //     x.id = 'textvalidator_cobot_input_dynamic_parameter';
                            //     x.autofocus =true;
                            //     x.onfocus=function (){
                            //         this.select();
                            //     }
                            //     x.maxLength = "5000";
                            //     x.cols = "50";
                            //     x.rows = "10";
                            //     // x.setAttribute("type", "text");
                            //     // x.setAttribute("value","");
                            //     x.style.marginRight = "10px"; 
                            //     x.style.height = "85px"; 
                            //     x.style.borderRadius = "4px"; 
                            //     x.style.border = "1px solid #ccc"; 
                            //     x.style.width = "100%";
                            //     var btn = document.createElement("BUTTON");
                            //     btn.id = 'textvalidator_cobot_input_button_for_dynamic_parameter';
                            //     btn.innerHTML = "VALIDATE";
                            //     btn.style.float = "right";
                            //     btn.style.padding = "5px 20px"; 
                            //     btn.style.background = "#336699"; 
                            //     btn.style.border = "1px solid #336699"; 
                            //     btn.style.marginTop = "8px"; 
                            //     btn.style.color = "#fff"; 
                            //     btn.onclick = function () {
                            //         if(String(e.value)!="" || inputVal){
                            //             // map.set(tacPreprocess(document.getElementById("cobot_input_dynamic_parameter").value), e.value);
                            //          //   hash[tacPreprocess(document.getElementById("cobot_input_dynamic_parameter").value)] = e.value;
                            //         // hash[tacPreprocess(e.target[0][0])] = document.getElementById("cobot_input_dynamic_parameter").value; 
                            //          //localStorage.clear();
                            //         //  if(sessionStorage .getItem("hash") == null){
                            //         //     var hash  = {};
                            //         //     hash[tacPreprocess(e.target[0][0])] = document.getElementById("cobot_input_dynamic_parameter").value; 
                            //         //     sessionStorage .setItem("hash", JSON.stringify(hash));
                            //         //  }else{
                            //         //     var hash  = JSON.parse(sessionStorage .getItem("hash"));
                            //         //     delete hash[e.target[0][0]];
                            //         //     hash[tacPreprocess(e.target[0][0])] = document.getElementById("cobot_input_dynamic_parameter").value; 
                            //         //     sessionStorage .setItem("hash",JSON.stringify(hash));
                            //         //  }
                            //         }
                            //         // let obj = Array.from(hash).reduce((obj, [key, value]) => (
                            //         //     Object.assign(obj, { [key]: value }) // Be careful! Maps can have non-String keys; object literals can't.
                            //         //   ), {});
                            //           var rqObj={};
                            //           rqObj.data = hash;
                            //           rqObj.flag = "input";
                            //           rqObj.url = COBOT_URL;
                            //           r.a.runtime.sendMessage(rqObj);      
                            //           console.log(rqObj);   
                            //           console.log(hash);                            
                            //         popupClose("textvalidator_cobot_overlay");
                            //         window.document.body.removeChild(iDiv)
                            //     };
                            //     _iDiv.appendChild(p);
                            //     _iDiv.appendChild(x);
                            //     _iDiv.appendChild(btn);
                            //     iDiv.appendChild(_iDiv);
                            //     window.document.body.appendChild(iDiv)
                            //     popupOpen("textvalidator_cobot_overlay");
                        }, !1), 
                        //window.document.body.appendChild(b),
                        // dragElement(document.getElementById("textvalidator_cobot_div")),

                        r.a.runtime.onMessage.addListener(function (e, t, n) {
                            var inputType = null;
                            var inputTypeCheck = false;
                            var inputVal = null;
                            console.log(e);

                            for (var i = 0; i < e.target.length; i++) {
                                var str = e.target[i][0];

                                if (inputType == null) {
                                    if (str.includes("@id")) {
                                        console.log(str);
                                        // console.log(getElementByXpath(e.target[i][0].replace("xpath=", "")));
                                        var element = getElementByXpath(e.target[i][0].replace("xpath=", ""));
                                        console.log(element);
                                        if (element != null) {
                                            inputType = element.getAttribute("type");
                                            var cCheck = element.checked;
                                            var dCheck = element.defaultChecked
                                            if (cCheck && dCheck) {
                                                inputTypeCheck = false;
                                            } else {
                                                inputTypeCheck = true;
                                            }
                                            inputVal = element.getAttribute("value")
                                        }


                                    }
                                }

                            }
                            console.log(inputType);
                            // console.log(inputTypeCheck);
                            // console.log(inputVal);

                            if (e.command == 'type' || e.command == 'select' || (inputType == 'radio' && inputTypeCheck) || (inputType == 'checkbox' && inputTypeCheck)) {
                                console.log("--" + e.target[0][0]);
                                //if(e.target[0][0]=="xpath=//input[@id='cobot_input_dynamic_parameter']" || e.target[0][0]=="xpath=//textarea[@id='textvalidator_cobot_input_dynamic_parameter']"){
                                if (e.target[0][0] == "xpath=//input[@id='cobot_input_dynamic_parameter']") {
                                    return
                                }
                                // console.log(e);
                                //if(enableCustomPopup){
                                    cobotPopup("param", e, inputType, r);
                                    if(enableCustomPopup){
                                    window.stop();
                                    }
                                //}
                                
                            } else {
                                delete hash[e.target[0][0]];
                                console.log(hash);
                                var rqObj = {};
                                rqObj.data = "";
                                rqObj.search = "";
                                rqObj.flag = "input";
                                rqObj.url = COBOT_URL;
                                r.a.runtime.sendMessage(rqObj);
                                e.recordNotification && (a.contentWindow.postMessage({
                                    direction: "from-recording-module",
                                    command: e.command,
                                    target: e.target,
                                    value: e.value
                                }, "*"), a.style.borderColor = "black", setTimeout(() => {
                                    a.style.borderColor = "#d30100"
                                }, 1e3), n(!0))
                            }

                        }), r.a.runtime.sendMessage({
                            setFrameNumberForTab: !0,
                            indicatorIndex: e
                        }).catch(() => { })
                }
            }

            function f() {
                "root" === l && a && (a.parentElement.removeChild(a), a = void 0)
            }
            async function g() {
                let e, t, n, i = window;
                for (; i !== window.top && (e = i.parent).frames.length;) {
                    e === window.top && (n = await r.a.runtime.sendMessage({
                        requestFrameCount: !0
                    }).catch(() => { })) && (t = n.indicatorIndex);
                    for (let n = 0; n < e.frames.length; n++) {
                        if (e.frames[n] === i) {
                            l = ":" + Object(o["a"])({
                                indicatorIndex: t,
                                targetFrameIndex: n
                            }) + l, i = e;
                            break
                        }
                    }
                }
                l = "root" + l, await r.a.runtime.sendMessage({
                    frameLocation: l
                }).catch(() => { })
            }

            function b(e, t, n, i, o) {
                var data = {
                    command: e,
                    target: t,
                    value: n,
                    insertBeforeLastCommand: i,
                    frameLocation: void 0 != o ? o : l,
                    commandSideexTabId: s
                };

                // for(var i=0;i<data.target.length;i++){
                //     var str = data.target[i][0];


                //         if(str.includes("@id")){
                //             console.log(str);
                //             console.log(data.target[i][0].replace("xpath=", ""));
                //             console.log(getElementByXpath(data.target[i][0].replace("xpath=", "")));
                //             var element = getElementByXpath(data.target[i][0].replace("xpath=", ""));
                //            inputType =   element.getAttribute("type");
                //           console.log("inputType->"+inputType)

                //         }


                // }
                r.a.runtime.sendMessage(data).catch(() => {
                    h.detach()
                })
            }
            r.a.runtime.onMessage.addListener(function (e, t, n) {
                e.attachRecorder && (h.attach(), n(!0))
            }), r.a.runtime.onMessage.addListener(function (e, t, n) {
                e.detachRecorder && (h.detach(), n(!0))
            }), r.a.runtime.onMessage.addListener(function (e, t, n) {
                if (e.recalculateFrameLocation) return (async () => {
                    f(), setTimeout(async () => {
                        await m()
                    }, 100), l = "", await g(), n(!0)
                })(), !0
            }), (async () => {
                await g()
            })(), window.recorder = h, window.contentSideexTabId = s, window.Recorder = c, window.record = b
        },
        423: function (e, t, n) {
            "use strict";
            t["a"] = function (e) {
                window === window.top && window.addEventListener("message", function (t) {
                    if (t.source.top == window && t.data && "from-page-script" == t.data.direction && t.data.recordedType) switch (t.data.recordedType) {
                        case "prompt":
                            null != t.data.recordedResult ? e("answerOnNextPrompt", [
                                [t.data.recordedResult]
                            ], "", !0, t.data.frameLocation) : e("chooseCancelOnNextPrompt", [
                                [""]
                            ], "", !0, t.data.frameLocation), e("assertPrompt", [
                                [t.data.recordedMessage]
                            ], "", !1, t.data.frameLocation), null != t.data.recordedResult ? e("webdriverAnswerOnVisiblePrompt", [
                                [t.data.recordedResult]
                            ], "", !1, t.data.frameLocation) : e("webdriverChooseCancelOnVisiblePrompt", [
                                [""]
                            ], "", !1, t.data.frameLocation);
                            break;
                        case "confirm":
                            1 == t.data.recordedResult ? e("chooseOkOnNextConfirmation", [
                                [""]
                            ], "", !0, t.data.frameLocation) : e("chooseCancelOnNextConfirmation", [
                                [""]
                            ], "", !0, t.data.frameLocation), e("assertConfirmation", [
                                [t.data.recordedMessage]
                            ], "", !1, t.data.frameLocation), 1 == t.data.recordedResult ? e("webdriverChooseOkOnVisibleConfirmation", [
                                [""]
                            ], "", !1, t.data.frameLocation) : e("webdriverChooseCancelOnVisibleConfirmation", [
                                [""]
                            ], "", !1, t.data.frameLocation);
                            break;
                        case "alert":
                            e("assertAlert", [
                                [t.data.recordedMessage]
                            ], "", !1, t.data.frameLocation)
                    }
                })
            }
        },
        45: function (e, t, n) {
            "use strict";
            var i = n(7);
            n.n(i);
            const r = i["environment"].isProduction;
            t["a"] = r;
            const o = i["environment"].isStaging;
            t["b"] = o;
            const a = i["environment"].isTest;
            t["c"] = a;
            const s = i["userAgent"].userAgent;
            t["d"] = s
        },
        52: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.preprocessParameter = function (e, t, n, {
                ignoreEscaping: i
            }) {
                const a = function (e, {
                    preprocessor: t,
                    ignoreEscaping: n
                }) {
                    return n ? e : t && "scriptPreprocessor" === t.name ? e.replace(/"/g, "'") : (0, r.default)(e)
                }(e, {
                    preprocessor: t,
                    ignoreEscaping: i
                });
                return t ? t(a, n) : o(a, n)
            }, t.defaultPreprocessor = o, t.scriptPreprocessor = function (e) {
                let t, n = e.replace(/^\s+/, "").replace(/\s+$/, ""),
                    i = [];
                const r = {},
                    o = [];
                let a = 0;
                if (/\$\{/.exec(n)) {
                    const e = /\$\{(.*?)\}/g;
                    let s = 0;
                    for (; t = e.exec(n);) {
                        const l = t[1];
                        t.index - s > 0 && i.push(n.substring(s, t.index)), r.hasOwnProperty(l) || (r[l] = a, o.push(l), a++), i.push(`arguments[${r[l]}]`), s = e.lastIndex
                    }
                    return s < n.length && i.push(n.substring(s, n.length)), {
                        script: i.join(""),
                        argv: o
                    }
                }
                return {
                    script: n,
                    argv: o
                }
            }, t.keysPreprocessor = function (e, t) {
                let n = [],
                    i = e.match(/\$\{\w+\}/g);
                if (i) {
                    let r = 0;
                    for (; r < e.length;) {
                        let a = i.shift(),
                            s = e.indexOf(a, r);
                        if (s > r && (n.push(e.substr(r, s - r)), r = s), a) {
                            if (/^\$\{KEY_\w+\}/.test(a)) {
                                let e = a.match(/\$\{KEY_(\w+)\}/)[1];
                                n.push(`Key['${e}']`)
                            } else n.push(o(a, t));
                            r += a.length
                        } else r < e.length && (n.push(e.substr(r, e.length)), r = e.length)
                    }
                } else n.push(e);
                return n
            };
            var i, r = (i = n(36)) && i.__esModule ? i : {
                default: i
            };

            function o(e, t) {
                if (!e) return;
                const n = e.match(/\$\{(\w+)\}/);
                return n ? e.replace(n[0], t(n[1])) : e
            }
        },
        53: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.default = o, t.renderCommands = a;
            var i, r = (i = n(54)) && i.__esModule ? i : {
                default: i
            };

            function o(e, t, {
                startingLevel: n,
                newLineCount: i,
                fullPayload: o,
                originTracing: s
            } = {}) {
                if (n || (n = 0), i || (i = 1), o || (o = !1), Array.isArray(t)) return a(t, {
                    startingLevel: n,
                    commandPrefixPadding: e,
                    originTracing: s
                }); {
                    const a = (0, r.default)(t, {
                        commandPrefixPadding: e,
                        startingLevel: n
                    });
                    return o ? a : a.body && a.body.length ? a.body + "\n".repeat(i) : ""
                }
            }

            function a(e, {
                startingLevel: t,
                commandPrefixPadding: n,
                originTracing: i
            } = {}) {
                let r = "",
                    a = t;
                const s = i ? i.splice(0, 2) : void 0;
                return s && (r += o(n, s.join("\n"), {
                    startingLevel: a
                })), e.forEach((e, t) => {
                    if (i) {
                        const e = o(n, i[t], {
                            startingLevel: a
                        });
                        r += e
                    }
                    if (e) {
                        const t = o(n, e, {
                            startingLevel: a,
                            fullPayload: !0
                        });
                        a = t.endingLevel, t.skipEmitting || (r += t.body, r += "\n")
                    }
                }), r
            }
        },
        54: function (e, t, n) {
            "use strict";

            function i({
                startingLevel: e,
                commandBlock: t
            } = {}) {
                let n = 0;
                return t.commands && t.commands.length > 0 && (n = t.commands[t.commands.length - 1].level || 0), e + n + (t.endingLevelAdjustment || 0)
            }
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.default = function (e, {
                startingLevel: t,
                commandPrefixPadding: n
            } = {}) {
                if (void 0 === e) return {
                    body: void 0
                };
                t || (t = 0);
                e.startingLevelAdjustment && (t += e.startingLevelAdjustment);
                t < 0 && (t = 0);
                return "object" == typeof e.commands ? e.skipEmitting ? {
                    endingLevel: i({
                        startingLevel: t,
                        commandBlock: e
                    }),
                    skipEmitting: e.skipEmitting
                } : {
                        body: e.commands.map(e => n.repeat(t + e.level) + e.statement).join("\n"),
                        endingLevel: i({
                            startingLevel: t,
                            commandBlock: e
                        })
                    } : {
                        body: e.split("\n").join("\n" + n.repeat(t)).replace(/^/, n.repeat(t)),
                        endingLevel: t
                    }
            }
        },
        55: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.registerCommandEmitter = function ({
                command: e,
                emitter: t,
                emitters: n
            } = {}) {
                if (n[e]) throw new Error("Unable to overwrite an existing command emitter");
                n[e] = t
            }, t.registerPreprocessors = function (e) {
                Object.keys(e).forEach(t => {
                    switch (t) {
                        case "sendKeys":
                            e[t].valuePreprocessor = i.keysPreprocessor;
                            break;
                        case "runScript":
                        case "executeScript":
                        case "executeAsyncScript":
                        case "if":
                        case "elseIf":
                        case "repeatIf":
                        case "while":
                            e[t].targetPreprocessor = i.scriptPreprocessor
                    }
                })
            }, t.registerMethod = async function (e, t, {
                generateMethodDeclaration: n,
                hooks: i
            }) {
                let r = n(e);
                r = "object" == typeof r ? r.body : r, await i.declareMethods.isRegistered(r) || t.forEach(e => {
                    i.declareMethods.register(() => e)
                })
            };
            var i = n(52)
        },
        56: function (e, t, n) {
            "use strict";

            function i(e, t, n) {
                const r = n || [];
                for (const n of e.commands)
                    if ("run" === n.command) {
                        const e = t.find(e => e.name === n.target);
                        return r.push({
                            name: e.name,
                            commands: e.commands
                        }), i(e, t, r)
                    } return r
            }
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.findReusedTestMethods = i, t.findCommandThatOpensWindow = function e(t, n) {
                let r;
                for (const o of t.commands) {
                    if (o.opensWindow) {
                        r = o;
                        break
                    }
                    if ("run" === o.command) {
                        const r = i(t, n);
                        for (const t in r) return e(r[t], r)
                    }
                }
                return r
            }
        },
        57: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.ArgTypes = void 0;
            const i = {
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
            t.ArgTypes = i
        },
        58: function (e, t) {
            e.exports = function (e) {
                return e.webpackPolyfill || (e.deprecate = function () { }, e.paths = [], e.children || (e.children = []), Object.defineProperty(e, "loaded", {
                    enumerable: !0,
                    get: function () {
                        return e.l
                    }
                }), Object.defineProperty(e, "id", {
                    enumerable: !0,
                    get: function () {
                        return e.i
                    }
                }), e.webpackPolyfill = 1), e
            }
        },
        7: function (e, t, n) {
            "use strict";
            var i = l(n(92)),
                r = l(n(36)),
                o = l(n(97)),
                a = l(n(99)),
                s = l(n(100));

            function l(e) {
                return e && e.__esModule ? e : {
                    default: e
                }
            }
            e.exports = {
                codeExport: i.default,
                stringEscape: r.default,
                userAgent: o.default,
                environment: a.default,
                project: s.default
            }
        },
        75: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.registerCommand = t.ArgTypes = t.TargetTypes = t.Commands = void 0;
            var i = n(94),
                r = n(57);
            const o = i.Commands;
            t.Commands = o;
            const a = i.TargetTypes;
            t.TargetTypes = a;
            const s = r.ArgTypes;
            t.ArgTypes = s;
            const l = i.registerCommand;
            t.registerCommand = l
        },
        92: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.default = void 0;
            var i = d(n(93)),
                r = d(n(54)),
                o = n(55),
                a = d(n(95)),
                s = d(n(56)),
                l = d(n(53)),
                c = d(n(96));

            function d(e) {
                return e && e.__esModule ? e : {
                    default: e
                }
            }

            function u(e) {
                for (var t = 1; t < arguments.length; t++) {
                    var n = null != arguments[t] ? arguments[t] : {},
                        i = Object.keys(n);
                    "function" == typeof Object.getOwnPropertySymbols && (i = i.concat(Object.getOwnPropertySymbols(n).filter(function (e) {
                        return Object.getOwnPropertyDescriptor(n, e).enumerable
                    }))), i.forEach(function (t) {
                        p(e, t, n[t])
                    })
                }
                return e
            }

            function p(e, t, n) {
                return t in e ? Object.defineProperty(e, t, {
                    value: n,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[t] = n, e
            }
            var h = {
                emit: u({}, i.default),
                prettify: r.default,
                register: {
                    preprocessors: o.registerPreprocessors,
                    emitter: o.registerCommandEmitter
                },
                hook: a.default,
                find: u({}, s.default),
                render: l.default,
                parsers: c.default
            };
            t.default = h
        },
        93: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.emitCommand = p, t.emitLocation = h, t.emitSelection = m, t.emitOriginTracing = b, t.default = void 0;
            var i = c(n(36)),
                r = n(52),
                o = c(n(53)),
                a = n(55),
                s = n(56),
                l = n(75);

            function c(e) {
                return e && e.__esModule ? e : {
                    default: e
                }
            }

            function d(e) {
                for (var t = 1; t < arguments.length; t++) {
                    var n = null != arguments[t] ? arguments[t] : {},
                        i = Object.keys(n);
                    "function" == typeof Object.getOwnPropertySymbols && (i = i.concat(Object.getOwnPropertySymbols(n).filter(function (e) {
                        return Object.getOwnPropertyDescriptor(n, e).enumerable
                    }))), i.forEach(function (t) {
                        u(e, t, n[t])
                    })
                }
                return e
            }

            function u(e, t, n) {
                return t in e ? Object.defineProperty(e, t, {
                    value: n,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[t] = n, e
            }

            function p(e, t, {
                variableLookup: n,
                emitNewWindowHandling: i
            } = {}) {
                if (function (e) {
                    const t = e.command;
                    if (!t.startsWith("//")) {
                        let n = l.Commands.find(e => e[0] === t);
                        if (!n) throw new Error(`Invalid command '${t}'`);
                        if (!!(n = n[1]).target != !!e.target && !n.target.isOptional) throw new Error(`Incomplete command '${e.command}'. Missing expected target argument.`);
                        if (!!n.value != !!e.value && !n.value.isOptional) throw new Error(`Incomplete command '${t}'. Missing expected value argument.`)
                    }
                }(e), t) {
                    const o = "storeJson" === e.command;
                    let a = t((0, r.preprocessParameter)(e.target, t.targetPreprocessor, n, {
                        ignoreEscaping: o
                    }), (0, r.preprocessParameter)(e.value, t.valuePreprocessor, n, {
                        ignoreEscaping: o
                    }));
                    return e.opensWindow && (a = i(e, a)), a
                }
            }

            function h(e, t) {
                if (/^\/\//.test(e)) return t.xpath(e);
                const n = e.split("="),
                    i = n.shift(),
                    r = n.join("=");
                if (t[i]) return t[i](r);
                throw new Error(i ? `Unknown locator ${i}` : "Locator can't be empty")
            }

            function m(e, t) {
                if (!e) throw new Error(`Location can't be empty`);
                const [n, i] = e.split("=");
                if (t[n] && i) {
                    return t[n](i)
                }
                if (i) throw new Error(`Unknown selection locator ${n}`);
                return t["label"](n)
            }
            async function f(e, t) {
                const n = e.map(e => t.emit(e));
                let i = [];
                return (await Promise.all(n)).forEach(e => {
                    "string" == typeof e && e.includes("\n") ? e.split("\n").forEach(e => {
                        i.push(e)
                    }) : i.push(e)
                }), i
            }
            async function g(e, {
                commandPrefixPadding: t,
                generateMethodDeclaration: n,
                terminatingKeyword: i,
                emitter: r,
                overrideCommandEmitting: o
            } = {}) {
                const a = n(e.name);
                let s, l = a,
                    c = i;
                return "object" == typeof a && (l = a.body, c = a.terminatingKeyword), [l, (s = o ? e.commands.map(e => `${t.repeat(e.level) + e.statement}`) : await f(e.commands, r)).join(`\n${t}`).replace(/^/, t), c]
            }

            function b(e, {
                commentPrefix: t
            }) {
                let n = [];
                return n.push(t + ` Test name: ${e.name}`), n.push(t + " Step # | name | target | value | comment"), e.commands.forEach((e, i) => {
                    n.push(t + ` ${i + 1} | ${e.command} | ${e.target} | ${e.value} | ${e.comment}`)
                }), n
            }
            async function w(e, t, {
                testLevel: n,
                commandLevel: i,
                testDeclaration: r,
                terminatingKeyword: l,
                commandPrefixPadding: c,
                commentPrefix: d,
                hooks: u,
                emitter: p,
                generateMethodDeclaration: h,
                enableOriginTracing: m,
                project: w
            } = {}) {
                let v = {};
                n = n || 1, i = i || 2;
                const y = (0, s.findReusedTestMethods)(e, t),
                    T = o.default.bind(this, c);
                if (p.extras)
                    for (const n in p.extras) {
                        let i = !0;
                        if ("emitWaitForWindow" === n && (0, s.findCommandThatOpensWindow)(e, t) && (i = !1), !i) {
                            const e = await p.extras[n](),
                                t = await g(e, {
                                    emitter: p,
                                    commandPrefixPadding: c,
                                    generateMethodDeclaration: e.generateMethodDeclaration,
                                    terminatingKeyword: l,
                                    overrideCommandEmitting: !0
                                });
                            await (0, a.registerMethod)(e.name, t, {
                                generateMethodDeclaration: e.generateMethodDeclaration,
                                hooks: u
                            })
                        }
                    }
                for (const e of y) {
                    const t = await g(e, {
                        emitter: p,
                        commandPrefixPadding: c,
                        generateMethodDeclaration: h,
                        terminatingKeyword: l
                    });
                    await (0, a.registerMethod)(e.name, t, {
                        generateMethodDeclaration: h,
                        hooks: u
                    })
                }
                const A = m ? b(e, {
                    commentPrefix: d
                }) : void 0;
                return v.testDeclaration = T(r, {
                    startingLevel: n
                }), v.inEachBegin = T(await u.inEachBegin.emit({
                    test: e,
                    tests: t,
                    project: w,
                    isOptional: !0
                }), {
                    startingLevel: i
                }), v.commands = T(await f(e.commands, p).catch(t => {
                    throw new Error(`Test '${e.name}' has a problem: ${t.message}`)
                }), {
                    startingLevel: i,
                    originTracing: A
                }), v.inEachEnd = T(await u.inEachEnd.emit({
                    test: e,
                    tests: t,
                    project: w,
                    isOptional: !0
                }), {
                    startingLevel: i
                }), v.testEnd = T(l, {
                    startingLevel: n
                }), v
            }
            var v = {
                command: p,
                commands: f,
                location: h,
                method: g,
                selection: m,
                suite: async function (e, t, {
                    suiteLevel: n,
                    testLevel: i,
                    suiteName: r,
                    suiteDeclaration: a,
                    terminatingKeyword: s,
                    commandPrefixPadding: l,
                    commentPrefix: c,
                    hooks: d,
                    suite: u,
                    project: p,
                    beforeEachOptions: h
                } = {}) {
                    let m = {};
                    i = i || 1, n = n || 0, u || (u = {
                        name: r
                    });
                    const f = o.default.bind(this, l);
                    return m.headerComment = c + " Generated by Selenium IDE\n", m.dependencies = f(await d.declareDependencies.emit({
                        suite: u,
                        tests: t,
                        project: p
                    })), m.suiteDeclaration = f(a, {
                        startingLevel: n
                    }), m.variables = f(await d.declareVariables.emit({
                        suite: u,
                        tests: t,
                        project: p
                    }), {
                        startingLevel: i
                    }), m.beforeAll = f(await d.beforeAll.emit({
                        suite: u,
                        tests: t,
                        project: p,
                        isOptional: !0
                    }), {
                        startingLevel: i
                    }), m.beforeEach = f(await d.beforeEach.emit({
                        suite: u,
                        tests: t,
                        project: p,
                        startingSyntaxOptions: h
                    }), {
                        startingLevel: i
                    }), m.afterEach = f(await d.afterEach.emit({
                        suite: u,
                        tests: t,
                        project: p
                    }), {
                        startingLevel: i
                    }), m.afterAll = f(await d.afterAll.emit({
                        suite: u,
                        tests: t,
                        project: p,
                        isOptional: !0
                    }), {
                        startingLevel: i
                    }), m.methods = f(await d.declareMethods.emit({
                        suite: u,
                        tests: t,
                        project: p,
                        isOptional: !0
                    }), {
                        startingLevel: i
                    }), m.tests = e, m.suiteEnd = f(s, {
                        startingLevel: n
                    }), d.declareMethods.clearRegister(), m
                },
                orderedSuite: function (e) {
                    let t = "";
                    if (t += e.headerComment, t += e.dependencies, t += e.suiteDeclaration, t += e.variables, t += e.beforeAll, t += e.beforeEach, t += e.afterEach, t += e.afterAll, t += e.methods, e.tests.testDeclaration) {
                        const n = e.tests;
                        t += n.testDeclaration, t += n.inEachBegin, t += n.commands, t += n.inEachEnd, t += n.testEnd
                    } else
                        for (const n in e.tests) {
                            const i = e.tests[n];
                            t += i.testDeclaration, t += i.inEachBegin, t += i.commands, t += i.inEachEnd, t += i.testEnd
                        }
                    return t += e.suiteEnd
                },
                test: w,
                text: i.default,
                testsFromSuite: async function (e, t, n, {
                    enableOriginTracing: i,
                    generateTestDeclaration: r,
                    project: o
                }) {
                    let a = {};
                    for (const s of t.tests) {
                        const t = e.find(e => e.name === s),
                            l = r(t.name);
                        a[t.name] = await w(t, e, d({}, n, {
                            testDeclaration: l,
                            enableOriginTracing: i,
                            project: o
                        }))
                    }
                    return a
                }
            };
            t.default = v
        },
        94: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.registerCommand = function (e, t) {
                if (s.find(e => e[0] === t.command)) throw new Error("Unable to overwrite existing command");
                s.push([e, t])
            }, t.Commands = t.TargetTypes = void 0;
            var i = n(57);

            function r(e) {
                for (var t = 1; t < arguments.length; t++) {
                    var n = null != arguments[t] ? arguments[t] : {},
                        i = Object.keys(n);
                    "function" == typeof Object.getOwnPropertySymbols && (i = i.concat(Object.getOwnPropertySymbols(n).filter(function (e) {
                        return Object.getOwnPropertyDescriptor(n, e).enumerable
                    }))), i.forEach(function (t) {
                        o(e, t, n[t])
                    })
                }
                return e
            }

            function o(e, t, n) {
                return t in e ? Object.defineProperty(e, t, {
                    value: n,
                    enumerable: !0,
                    configurable: !0,
                    writable: !0
                }) : e[t] = n, e
            }
            const a = {
                NONE: 0,
                LOCATOR: "locator",
                REGION: "region"
            };
            t.TargetTypes = a;
            const s = [
                ["addSelection", {
                    name: "add selection",
                    description: `Add a selection to the set of options in a multi-select element.`,
                    type: a.LOCATOR,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.value
                }],
                ["answerOnNextPrompt", {
                    name: "answer on next prompt",
                    description: `Affects the next alert prompt. This command will send the \n        specified answer string to it. If the alert is already present, then use \n        "webdriver answer on visible prompt" instead.`,
                    target: i.ArgTypes.answer
                }],
                ["assert", {
                    name: "assert",
                    description: `Check that a variable is an expected value. The variable's \n        value will be converted to a string for comparison. The test will stop if the assert fails.`,
                    target: i.ArgTypes.variableName,
                    value: i.ArgTypes.expectedValue
                }],
                ["assertAlert", {
                    name: "assert alert",
                    description: `Confirm that an alert has been rendered with the provided text. The test will stop if the assert fails.`,
                    target: i.ArgTypes.alertText
                }],
                ["assertChecked", {
                    name: "assert checked",
                    type: a.LOCATOR,
                    description: "Confirm that the target element has been checked. The test will stop if the assert fails.",
                    target: i.ArgTypes.locator
                }],
                ["assertConfirmation", {
                    name: "assert confirmation",
                    description: "Confirm that a confirmation has been rendered. The test will stop if the assert fails.",
                    target: i.ArgTypes.text
                }],
                ["assertEditable", {
                    name: "assert editable",
                    type: a.LOCATOR,
                    description: "Confirm that the target element is editable. The test will stop if the assert fails.",
                    target: i.ArgTypes.locator
                }],
                ["assertElementPresent", {
                    name: "assert element present",
                    type: a.LOCATOR,
                    description: `Confirm that the target element is present somewhere on the page. The test will stop if the assert fails.`,
                    target: i.ArgTypes.locator
                }],
                ["assertElementNotPresent", {
                    name: "assert element not present",
                    type: a.LOCATOR,
                    description: `Confirm that the target element is not present anywhere on the page. The test will stop if the assert fails.`,
                    target: i.ArgTypes.locator
                }],
                ["assertNotChecked", {
                    name: "assert not checked",
                    type: a.LOCATOR,
                    description: "Confirm that the target element has not been checked. The test will stop if the assert fails.",
                    target: i.ArgTypes.locator
                }],
                ["assertNotEditable", {
                    name: "assert not editable",
                    type: a.LOCATOR,
                    description: "Confirm that the target element is not editable. The test will stop if the assert fails.",
                    target: i.ArgTypes.locator
                }],
                ["assertNotSelectedValue", {
                    name: "assert not selected value",
                    type: a.LOCATOR,
                    description: `Confirm that the value attribute of the selected option \n        in a dropdown element does not contain the provided value. The test will stop if the assert fails.`,
                    target: i.ArgTypes.selectLocator,
                    value: i.ArgTypes.pattern
                }],
                ["assertNotText", {
                    name: "assert not text",
                    type: a.LOCATOR,
                    description: `Confirm that the text of an element does not contain the provided value.\n      The test will stop if the assert fails.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.pattern
                }],
                ["assertPrompt", {
                    name: "assert prompt",
                    description: "Confirm that a JavaScript prompt has been rendered. The test will stop if the assert fails.",
                    target: i.ArgTypes.text
                }],
                ["assertSelectedValue", {
                    name: "assert selected value",
                    type: a.LOCATOR,
                    description: `Confirm that the value attribute of the selected option \n        in a dropdown element contains the provided value. The test will stop if the assert fails.`,
                    target: i.ArgTypes.selectLocator,
                    value: i.ArgTypes.pattern
                }],
                ["assertSelectedLabel", {
                    name: "assert selected label",
                    type: a.LOCATOR,
                    description: `Confirm that the label of the selected option in a dropdown \n        element contains the provided value. The test will stop if the assert fails.`,
                    target: i.ArgTypes.selectLocator,
                    value: i.ArgTypes.pattern
                }],
                ["assertText", {
                    name: "assert text",
                    type: a.LOCATOR,
                    description: `Confirm that the text of an element contains the provided value.\n      The test will stop if the assert fails.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.pattern
                }],
                ["assertTitle", {
                    name: "assert title",
                    description: `Confirm the title of the current page contains the provided text.\n      The test will stop if the assert fails.`,
                    target: i.ArgTypes.pattern
                }],
                ["assertValue", {
                    name: "assert value",
                    type: a.LOCATOR,
                    description: `Confirm the (whitespace-trimmed) value of an input field \n        (or anything else with a value parameter). For checkbox/radio elements, \n        the value will be "on" or "off" depending on whether the element is \n        checked or not. The test will stop if the assert fails.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.pattern
                }],
                ["check", {
                    name: "check",
                    type: a.LOCATOR,
                    description: "Check a toggle-button (checkbox/radio).",
                    target: i.ArgTypes.locator
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
                    type: a.LOCATOR,
                    description: `Clicks on a target element (e.g., a link, button, checkbox, or radio button).`,
                    target: i.ArgTypes.locator
                }],
                ["clickAt", {
                    name: "click at",
                    type: a.LOCATOR,
                    description: `Clicks on a target element (e.g., a link, button, checkbox, \n        or radio button). The coordinates are relative to the target element \n        (e.g., 0,0 is the top left corner of the element) and are mostly used \n        to check effects that relay on them, for example the material ripple effect.`,
                    target: i.ArgTypes.locator,
                    value: r({
                        isOptional: !0
                    }, i.ArgTypes.coord)
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
                    type: a.LOCATOR,
                    description: `Double clicks on an element (e.g., a link, button, checkbox, or radio button).`,
                    target: i.ArgTypes.locator
                }],
                ["doubleClickAt", {
                    name: "double click at",
                    type: a.LOCATOR,
                    description: `Double clicks on a target element (e.g., a link, button, \n        checkbox, or radio button). The coordinates are relative to the target \n        element (e.g., 0,0 is the top left corner of the element) and are mostly \n        used to check effects that relay on them, for example the material \n        ripple effect.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.coord
                }],
                ["dragAndDropToObject", {
                    name: "drag and drop to object",
                    type: a.LOCATOR,
                    description: "Drags an element and drops it on another element.",
                    target: i.ArgTypes.locatorOfObjectToBeDragged,
                    value: i.ArgTypes.locatorOfDragDestinationObject
                }],
                ["echo", {
                    name: "echo",
                    description: `Prints the specified message into the third table cell in \n        your Selenese tables. Useful for debugging.`,
                    target: i.ArgTypes.message
                }],
                ["editContent", {
                    name: "edit content",
                    type: a.LOCATOR,
                    description: `Sets the value of a content editable element as if you typed in it.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.value
                }],
                ["else", {
                    name: "else",
                    description: `Part of an if block. Execute the commands in this branch \n        when an if and/or else if condition are not met. Terminate the branch \n        with the end command.`
                }],
                ["elseIf", {
                    name: "else if",
                    description: `Part of an if block. Execute the commands in this branch \n        when an if condition has not been met. Terminate the branch with the \n        end command.`,
                    target: i.ArgTypes.conditionalExpression
                }],
                ["end", {
                    name: "end",
                    description: `Terminates a control flow block for if, while, and times.`
                }],
                ["executeScript", {
                    name: "execute script",
                    description: `Executes a snippet of JavaScript in the context of the \n        currently selected frame or window. The script fragment will be executed \n        as the body of an anonymous function.  To store the return value, use \n        the 'return' keyword and provide a variable name in the value input field.`,
                    target: i.ArgTypes.script,
                    value: r({
                        isOptional: !0
                    }, i.ArgTypes.variableName)
                }],
                ["executeAsyncScript", {
                    name: "execute async script",
                    description: `Executes an async snippet of JavaScript in the context of \n        the currently selected frame or window. The script fragment will be \n        executed as the body of an anonymous function and must return a Promise. \n        The Promise result will be saved on the variable if you use the 'return' \n        keyword.`,
                    target: i.ArgTypes.script,
                    value: r({
                        isOptional: !0
                    }, i.ArgTypes.variableName)
                }],
                ["forEach", {
                    name: "for each",
                    description: `Create a loop that executes the proceeding commands for each item in a given collection.`,
                    target: i.ArgTypes.arrayVariableName,
                    value: i.ArgTypes.iteratorVariableName
                }],
                ["if", {
                    name: "if",
                    type: a.LOCATOR,
                    description: `Create a conditional branch in your test. Terminate the branch with the end command.`,
                    target: i.ArgTypes.conditionalExpression
                }],
                ["mouseDown", {
                    name: "mouse down",
                    type: a.LOCATOR,
                    description: `Simulates a user pressing the left mouse button (without \n        releasing it yet).`,
                    target: i.ArgTypes.locator
                }],
                ["mouseDownAt", {
                    name: "mouse down at",
                    type: a.LOCATOR,
                    description: `Simulates a user pressing the left mouse button (without \n        releasing it yet) at the specified location.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.coord
                }],
                ["mouseMoveAt", {
                    name: "mouse move at",
                    type: a.LOCATOR,
                    description: `Simulates a user pressing the mouse button (without releasing \n        it yet) on the specified element.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.coord
                }],
                ["mouseOut", {
                    name: "mouse out",
                    type: a.LOCATOR,
                    description: `Simulates a user moving the mouse pointer away from the specified element.`,
                    target: i.ArgTypes.locator
                }],
                ["mouseOver", {
                    name: "mouse over",
                    type: a.LOCATOR,
                    description: `Simulates a user hovering a mouse over the specified element.`,
                    target: i.ArgTypes.locator
                }],
                ["mouseUp", {
                    name: "mouse up",
                    type: a.LOCATOR,
                    description: `Simulates the event that occurs when the user releases the \n        mouse button (e.g., stops holding the button down).`,
                    target: i.ArgTypes.locator
                }],
                ["mouseUpAt", {
                    name: "mouse up at",
                    type: a.LOCATOR,
                    description: `Simulates the event that occurs when the user releases the \n        mouse button (e.g., stops holding the button down) at the specified location.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.coord
                }],
                ["open", {
                    name: "open",
                    description: `Opens a URL and waits for the page to load before proceeding. \n        This accepts both relative and absolute URLs.`,
                    target: i.ArgTypes.url
                }],
                ["pause", {
                    name: "pause",
                    description: "Wait for the specified amount of time.",
                    target: i.ArgTypes.waitTime
                }],
                ["removeSelection", {
                    name: "remove selection",
                    type: a.LOCATOR,
                    description: `Remove a selection from the set of selected options in a \n        multi-select element using an option locator.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.optionLocator
                }],
                ["repeatIf", {
                    name: "repeat if",
                    description: `Terminate a 'do' control flow branch conditionally. If \n        the result of the provided conditional expression is true, it starts \n        the do loop over.  Otherwise it ends the loop.`,
                    target: i.ArgTypes.conditionalExpression,
                    value: r({
                        isOptional: !0
                    }, i.ArgTypes.loopLimit)
                }],
                ["run", {
                    name: "run",
                    description: "Runs a test case from the current project.",
                    target: i.ArgTypes.testCase,
                    value: r({
                        isOptional: !0
                    }, i.ArgTypes.optionalFlag)
                }],
                ["runScript", {
                    name: "run script",
                    description: `Creates a new "script" tag in the body of the current \n        test window, and adds the specified text into the body of the command. \n        Beware that JS exceptions thrown in these script tags aren't managed \n        by Selenium, so you should probably wrap your script in try/catch blocks \n        if there is any chance that the script will throw an exception.`,
                    target: i.ArgTypes.script
                }],
                ["select", {
                    name: "select",
                    type: a.LOCATOR,
                    description: `Select an element from a drop-down menu using an option \n        locator. Option locators provide different ways of specifying a select \n        element (e.g., label=, value=, id=, index=). If no option locator prefix \n        is provided, a match on the label will be attempted.`,
                    target: i.ArgTypes.selectLocator,
                    value: i.ArgTypes.optionLocator
                }],
                ["selectFrame", {
                    name: "select frame",
                    type: a.LOCATOR,
                    description: `Selects a frame within the current window. You can select a\n        frame by its 0-based index number (e.g., select the first frame with \n        "index=0", or the third frame with "index=2"). For nested frames you will\n        need to invoke this command multiple times (once for each frame in the \n        tree until you reach your desired frame). You can select the parent \n        frame with "relative=parent". To return to the top of the page use \n        "relative=top".`,
                    target: i.ArgTypes.locator
                }],
                ["selectWindow", {
                    name: "select window",
                    description: `Selects a popup window using a window locator. Once a \n        popup window has been selected, all commands will go to that window. \n        Window locators use handles to select windows.`,
                    target: i.ArgTypes.handle
                }],
                ["sendKeys", {
                    name: "send keys",
                    type: a.LOCATOR,
                    description: `Simulates keystroke events on the specified element, as \n        though you typed the value key-by-key. This simulates a real user typing \n        every character in the specified string; it is also bound by the \n        limitations of a real user, like not being able to type into a invisible \n        or read only elements.  This is useful for dynamic UI widgets (like \n        auto-completing combo boxes) that require explicit key events. Unlike \n        the simple "type" command, which forces the specified value into the \n        page directly, this command will not replace the existing content.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.keySequence
                }],
                ["setSpeed", {
                    name: "set speed",
                    description: `Set execution speed (e.g., set the millisecond length of \n        a delay which will follow each Selenium operation). By default, there \n        is no such delay, e.g., the delay is 0 milliseconds. This setting is \n        global, and will affect all test runs, until changed.`,
                    target: i.ArgTypes.waitTime
                }],
                ["setWindowSize", {
                    name: "set window size",
                    description: "Set the browser's window size, including the browser's interface.",
                    target: i.ArgTypes.resolution
                }],
                ["store", {
                    name: "store",
                    description: "Save a target string as a variable for easy re-use.",
                    target: i.ArgTypes.text,
                    value: i.ArgTypes.variableName
                }],
                ["storeAttribute", {
                    name: "store attribute",
                    description: `Gets the value of an element attribute. The value of the \n        attribute may differ across browsers (this is the case for the "style" \n        attribute, for example).`,
                    target: i.ArgTypes.attributeLocator,
                    value: i.ArgTypes.variableName
                }],
                ["storeJson", {
                    name: "store json",
                    description: ``,
                    target: i.ArgTypes.json,
                    value: i.ArgTypes.variableName
                }],
                ["storeText", {
                    name: "store text",
                    type: a.LOCATOR,
                    description: `Gets the text of an element and stores it for later use. \n        This works for any element that contains text.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.variableName
                }],
                ["storeTitle", {
                    name: "store title",
                    description: "Gets the title of the current page.",
                    target: i.ArgTypes.text,
                    value: i.ArgTypes.variableName
                }],
                ["storeValue", {
                    name: "store value",
                    type: a.LOCATOR,
                    description: `Gets the value of element and stores it for later use. \n        This works for any input type element.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.variableName
                }],
                ["storeWindowHandle", {
                    name: "store window handle",
                    description: "Gets the handle of the current page.",
                    target: i.ArgTypes.handle
                }],
                ["storeXpathCount", {
                    name: "store xpath count",
                    description: `Gets the number of nodes that match the specified xpath \n        (e.g. "//table" would give the number of tables).`,
                    target: i.ArgTypes.xpath,
                    value: i.ArgTypes.variableName
                }],
                ["submit", {
                    name: "submit",
                    type: a.LOCATOR,
                    description: `Submit the specified form. This is particularly useful for \n        forms without submit buttons, e.g. single-input "Search" forms.`,
                    target: i.ArgTypes.formLocator
                }],
                ["times", {
                    name: "times",
                    description: `Create a loop that executes the proceeding commands n number of times.`,
                    target: i.ArgTypes.times,
                    value: r({
                        isOptional: !0
                    }, i.ArgTypes.loopLimit)
                }],
                ["type", {
                    name: "type",
                    type: a.LOCATOR,
                    description: `Sets the value of an input field, as though you typed it \n        in. Can also be used to set the value of combo boxes, check boxes, etc. \n        In these cases, value should be the value of the option selected, not \n        the visible text.  Chrome only: If a file path is given it will be \n        uploaded to the input (for type=file), NOTE: XPath locators are not \n        supported.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.value
                }],
                ["uncheck", {
                    name: "uncheck",
                    type: a.LOCATOR,
                    description: "Uncheck a toggle-button (checkbox/radio).",
                    target: i.ArgTypes.locator
                }],
                ["verify", {
                    name: "verify",
                    description: `Soft assert that a variable is an expected value. The \n        variable's value will be converted to a string for comparison.\n        The test will continue even if the verify fails.`,
                    target: i.ArgTypes.variableName,
                    value: i.ArgTypes.expectedValue
                }],
                ["verifyChecked", {
                    name: "verify checked",
                    type: a.LOCATOR,
                    description: `Soft assert that a toggle-button (checkbox/radio) has been checked.\n      The test will continue even if the verify fails.`,
                    target: i.ArgTypes.locator
                }],
                ["verifyEditable", {
                    name: "verify editable",
                    type: a.LOCATOR,
                    description: `Soft assert whether the specified input element is \n        editable (e.g., hasn't been disabled). The test will continue even if the verify fails.`,
                    target: i.ArgTypes.locator
                }],
                ["verifyElementPresent", {
                    name: "verify element present",
                    type: a.LOCATOR,
                    description: `Soft assert that the specified element is somewhere on the page.\n      The test will continue even if the verify fails.`,
                    target: i.ArgTypes.locator
                }],
                ["verifyElementNotPresent", {
                    name: "verify element not present",
                    type: a.LOCATOR,
                    description: `Soft assert that the specified element is not somewhere on the page.\n      The test will continue even if the verify fails.`,
                    target: i.ArgTypes.locator
                }],
                ["verifyNotChecked", {
                    name: "verify not checked",
                    type: a.LOCATOR,
                    description: `Soft assert that a toggle-button (checkbox/radio) has not been checked.\n      The test will continue even if the verify fails.`,
                    target: i.ArgTypes.locator
                }],
                ["verifyNotEditable", {
                    name: "verify not editable",
                    type: a.LOCATOR,
                    description: `Soft assert whether the specified input element is not \n        editable (e.g., hasn't been disabled). The test will continue even if the verify fails.`,
                    target: i.ArgTypes.locator
                }],
                ["verifyNotSelectedValue", {
                    name: "verify not selected value",
                    description: `Soft assert that the expected element has not been chosen \n        in a select menu by its option attribute. The test will continue even if the verify fails.`,
                    target: i.ArgTypes.selectLocator,
                    value: i.ArgTypes.optionLocator
                }],
                ["verifyNotText", {
                    name: "verify not text",
                    type: a.LOCATOR,
                    description: "Soft assert the text of an element is not present. The test will continue even if the verify fails.",
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.text
                }],
                ["verifySelectedLabel", {
                    name: "verify selected label",
                    type: a.LOCATOR,
                    description: `Soft assert the visible text for a selected option in the \n        specified select element. The test will continue even if the verify fails.`,
                    target: i.ArgTypes.selectLocator,
                    value: i.ArgTypes.pattern
                }],
                ["verifySelectedValue", {
                    name: "verify selected value",
                    type: a.LOCATOR,
                    description: `Soft assert that the expected element has been chosen in \n        a select menu by its option attribute. The test will continue even if the verify fails.`,
                    target: i.ArgTypes.selectLocator,
                    value: i.ArgTypes.optionLocator
                }],
                ["verifyText", {
                    name: "verify text",
                    type: a.LOCATOR,
                    description: "Soft assert the text of an element is present. The test will continue even if the verify fails.",
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.text
                }],
                ["verifyTitle", {
                    name: "verify title",
                    description: "Soft assert the title of the current page contains the provided text. The test will continue even if the verify fails.",
                    target: i.ArgTypes.text
                }],
                ["verifyValue", {
                    name: "verify value",
                    type: a.LOCATOR,
                    description: `Soft assert the (whitespace-trimmed) value of an input \n        field (or anything else with a value parameter). For checkbox/radio \n        elements, the value will be "on" or "off" depending on whether the \n        element is checked or not. The test will continue even if the verify fails.`,
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.pattern
                }],
                ["waitForElementEditable", {
                    name: "wait for element editable",
                    type: a.LOCATOR,
                    description: "Wait for an element to be editable.",
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.waitTime
                }],
                ["waitForElementNotEditable", {
                    name: "wait for element not editable",
                    type: a.LOCATOR,
                    description: "Wait for an element to not be editable.",
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.waitTime
                }],
                ["waitForElementNotPresent", {
                    name: "wait for element not present",
                    type: a.LOCATOR,
                    description: "Wait for a target element to not be present on the page.",
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.waitTime
                }],
                ["waitForElementNotVisible", {
                    name: "wait for element not visible",
                    type: a.LOCATOR,
                    description: "Wait for a target element to not be visible on the page.",
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.waitTime
                }],
                ["waitForElementPresent", {
                    name: "wait for element present",
                    type: a.LOCATOR,
                    description: "Wait for a target element to be present on the page.",
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.waitTime
                }],
                ["waitForElementVisible", {
                    name: "wait for element visible",
                    type: a.LOCATOR,
                    description: "Wait for a target element to be visible on the page.",
                    target: i.ArgTypes.locator,
                    value: i.ArgTypes.waitTime
                }],
                ["webdriverAnswerOnVisiblePrompt", {
                    name: "webdriver answer on visible prompt",
                    description: `Affects a currently showing alert prompt. This command \n        instructs Selenium to provide the specified answer to it. If the alert \n        has not appeared yet then use "answer on next prompt" instead.`,
                    target: i.ArgTypes.answer
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
                    target: i.ArgTypes.conditionalExpression,
                    value: r({
                        isOptional: !0
                    }, i.ArgTypes.loopLimit)
                }]
            ];
            t.Commands = s
        },
        95: function (e, t, n) {
            "use strict";
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.clearHooks = function (e) {
                Object.keys(e).forEach(t => {
                    e[t].clearRegister()
                })
            }, t.default = void 0;
            t.default = class {
                constructor({
                    startingSyntax: e,
                    endingSyntax: t,
                    registrationLevel: n
                } = {}) {
                    this.startingSyntax = e, this.endingSyntax = t, this.registrationLevel = n, this.clearRegister = this.clearRegister.bind(this), this.emit = this.emit.bind(this), this.register = this.register.bind(this), this.isRegistered = this.isRegistered.bind(this), this.clearRegister()
                }
                clearRegister() {
                    this.emitters = []
                }
                async emit({
                    isOptional: e,
                    test: t,
                    suite: n,
                    tests: i,
                    project: r,
                    startingSyntaxOptions: o
                } = {
                        isOptional: !1
                    }) {
                    const a = [];
                    let s = 0;
                    if (this.startingSyntax) {
                        const e = "function" == typeof this.startingSyntax ? this.startingSyntax(o) : this.startingSyntax;
                        e.commands ? e.commands.forEach(e => {
                            a.push(e), s = e.level
                        }) : a.push({
                            level: 0,
                            statement: e
                        })
                    }
                    const l = t ? t.name : n ? n.name : void 0,
                        c = (await Promise.all(this.emitters.map(e => e({
                            name: l,
                            tests: i,
                            project: r
                        })))).filter(e => void 0 != e);
                    if (!e || c.length) return c.forEach(e => {
                        "object" == typeof e ? a.push(e) : "string" == typeof e && e.split("\n").forEach(e => {
                            a.push({
                                level: this.registrationLevel ? this.registrationLevel : s,
                                statement: e
                            })
                        })
                    }), this.endingSyntax && (this.endingSyntax.commands ? this.endingSyntax.commands.forEach(e => {
                        a.push(e)
                    }) : a.push({
                        level: 0,
                        statement: this.endingSyntax
                    })), {
                        commands: a
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
        96: function (e, t, n) {
            "use strict";

            function i(e) {
                return e.replace(/([^a-z0-9]+)/gi, "")
            }

            function r(e) {
                return e.charAt(0).toUpperCase() + e.substr(1)
            }

            function o(e) {
                return e.charAt(0).toLowerCase() + e.substr(1)
            }
            Object.defineProperty(t, "__esModule", {
                value: !0
            }), t.sanitizeName = i, t.capitalize = r, t.uncapitalize = o, t.default = void 0;
            var a = {
                sanitizeName: i,
                capitalize: r,
                uncapitalize: o
            };
            t.default = a
        },
        97: function (e, t, n) {
            "use strict";
            var i, r = (i = n(98)) && i.__esModule ? i : {
                default: i
            };
            const o = (() => {
                try {
                    return (0, r.default)(window.navigator.userAgent)
                } catch (e) {
                    return !1
                }
            })(),
                a = o && o.browser ? o.browser : void 0,
                s = a && "Chrome" === a.name,
                l = a && "Firefox" === a.name,
                c = s || l ? a.name : void 0;
            e.exports = {
                userAgent: o,
                browserName: c,
                isChrome: s,
                isFirefox: l
            }
        },
        98: function (e, t, n) {
            var i;
            ! function (r, o) {
                "use strict";
                var a = "",
                    s = "?",
                    l = "function",
                    c = "undefined",
                    d = "object",
                    u = "string",
                    p = "model",
                    h = "name",
                    m = "type",
                    f = "vendor",
                    g = "version",
                    b = "architecture",
                    w = "console",
                    v = "mobile",
                    y = "tablet",
                    T = "smarttv",
                    A = "wearable",
                    O = {
                        extend: function (e, t) {
                            var n = {};
                            for (var i in e) t[i] && t[i].length % 2 == 0 ? n[i] = t[i].concat(e[i]) : n[i] = e[i];
                            return n
                        },
                        has: function (e, t) {
                            return "string" == typeof e && -1 !== t.toLowerCase().indexOf(e.toLowerCase())
                        },
                        lowerize: function (e) {
                            return e.toLowerCase()
                        },
                        major: function (e) {
                            return typeof e === u ? e.replace(/[^\d\.]/g, "").split(".")[0] : o
                        },
                        trim: function (e) {
                            return e.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "")
                        }
                    },
                    x = {
                        rgx: function (e, t) {
                            for (var n, i, r, a, s, c, u = 0; u < t.length && !s;) {
                                var p = t[u],
                                    h = t[u + 1];
                                for (n = i = 0; n < p.length && !s;)
                                    if (s = p[n++].exec(e))
                                        for (r = 0; r < h.length; r++) c = s[++i], typeof (a = h[r]) === d && a.length > 0 ? 2 == a.length ? typeof a[1] == l ? this[a[0]] = a[1].call(this, c) : this[a[0]] = a[1] : 3 == a.length ? typeof a[1] !== l || a[1].exec && a[1].test ? this[a[0]] = c ? c.replace(a[1], a[2]) : o : this[a[0]] = c ? a[1].call(this, c, a[2]) : o : 4 == a.length && (this[a[0]] = c ? a[3].call(this, c.replace(a[1], a[2])) : o) : this[a] = c || o;
                                u += 2
                            }
                        },
                        str: function (e, t) {
                            for (var n in t)
                                if (typeof t[n] === d && t[n].length > 0) {
                                    for (var i = 0; i < t[n].length; i++)
                                        if (O.has(t[n][i], e)) return n === s ? o : n
                                } else if (O.has(t[n], e)) return n === s ? o : n;
                            return e
                        }
                    },
                    C = {
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
                    S = {
                        browser: [
                            [/(opera\smini)\/([\w\.-]+)/i, /(opera\s[mobiletab]+).+version\/([\w\.-]+)/i, /(opera).+version\/([\w\.]+)/i, /(opera)[\/\s]+([\w\.]+)/i],
                            [h, g],
                            [/(opios)[\/\s]+([\w\.]+)/i],
                            [
                                [h, "Opera Mini"], g
                            ],
                            [/\s(opr)\/([\w\.]+)/i],
                            [
                                [h, "Opera"], g
                            ],
                            [/(kindle)\/([\w\.]+)/i, /(lunascape|maxthon|netfront|jasmine|blazer)[\/\s]?([\w\.]*)/i, /(avant\s|iemobile|slim|baidu)(?:browser)?[\/\s]?([\w\.]*)/i, /(?:ms|\()(ie)\s([\w\.]+)/i, /(rekonq)\/([\w\.]*)/i, /(chromium|flock|rockmelt|midori|epiphany|silk|skyfire|ovibrowser|bolt|iron|vivaldi|iridium|phantomjs|bowser|quark|qupzilla|falkon)\/([\w\.-]+)/i],
                            [h, g],
                            [/(konqueror)\/([\w\.]+)/i],
                            [
                                [h, "Konqueror"], g
                            ],
                            [/(trident).+rv[:\s]([\w\.]+).+like\sgecko/i],
                            [
                                [h, "IE"], g
                            ],
                            [/(edge|edgios|edga|edg)\/((\d+)?[\w\.]+)/i],
                            [
                                [h, "Edge"], g
                            ],
                            [/(yabrowser)\/([\w\.]+)/i],
                            [
                                [h, "Yandex"], g
                            ],
                            [/(puffin)\/([\w\.]+)/i],
                            [
                                [h, "Puffin"], g
                            ],
                            [/(focus)\/([\w\.]+)/i],
                            [
                                [h, "Firefox Focus"], g
                            ],
                            [/(opt)\/([\w\.]+)/i],
                            [
                                [h, "Opera Touch"], g
                            ],
                            [/((?:[\s\/])uc?\s?browser|(?:juc.+)ucweb)[\/\s]?([\w\.]+)/i],
                            [
                                [h, "UCBrowser"], g
                            ],
                            [/(comodo_dragon)\/([\w\.]+)/i],
                            [
                                [h, /_/g, " "], g
                            ],
                            [/(windowswechat qbcore)\/([\w\.]+)/i],
                            [
                                [h, "WeChat(Win) Desktop"], g
                            ],
                            [/(micromessenger)\/([\w\.]+)/i],
                            [
                                [h, "WeChat"], g
                            ],
                            [/(brave)\/([\w\.]+)/i],
                            [
                                [h, "Brave"], g
                            ],
                            [/(qqbrowserlite)\/([\w\.]+)/i],
                            [h, g],
                            [/(QQ)\/([\d\.]+)/i],
                            [h, g],
                            [/m?(qqbrowser)[\/\s]?([\w\.]+)/i],
                            [h, g],
                            [/(BIDUBrowser)[\/\s]?([\w\.]+)/i],
                            [h, g],
                            [/(2345Explorer)[\/\s]?([\w\.]+)/i],
                            [h, g],
                            [/(MetaSr)[\/\s]?([\w\.]+)/i],
                            [h],
                            [/(LBBROWSER)/i],
                            [h],
                            [/xiaomi\/miuibrowser\/([\w\.]+)/i],
                            [g, [h, "MIUI Browser"]],
                            [/;fbav\/([\w\.]+);/i],
                            [g, [h, "Facebook"]],
                            [/safari\s(line)\/([\w\.]+)/i, /android.+(line)\/([\w\.]+)\/iab/i],
                            [h, g],
                            [/headlesschrome(?:\/([\w\.]+)|\s)/i],
                            [g, [h, "Chrome Headless"]],
                            [/\swv\).+(chrome)\/([\w\.]+)/i],
                            [
                                [h, /(.+)/, "$1 WebView"], g
                            ],
                            [/((?:oculus|samsung)browser)\/([\w\.]+)/i],
                            [
                                [h, /(.+(?:g|us))(.+)/, "$1 $2"], g
                            ],
                            [/android.+version\/([\w\.]+)\s+(?:mobile\s?safari|safari)*/i],
                            [g, [h, "Android Browser"]],
                            [/(sailfishbrowser)\/([\w\.]+)/i],
                            [
                                [h, "Sailfish Browser"], g
                            ],
                            [/(chrome|omniweb|arora|[tizenoka]{5}\s?browser)\/v?([\w\.]+)/i],
                            [h, g],
                            [/(dolfin)\/([\w\.]+)/i],
                            [
                                [h, "Dolphin"], g
                            ],
                            [/((?:android.+)crmo|crios)\/([\w\.]+)/i],
                            [
                                [h, "Chrome"], g
                            ],
                            [/(coast)\/([\w\.]+)/i],
                            [
                                [h, "Opera Coast"], g
                            ],
                            [/fxios\/([\w\.-]+)/i],
                            [g, [h, "Firefox"]],
                            [/version\/([\w\.]+).+?mobile\/\w+\s(safari)/i],
                            [g, [h, "Mobile Safari"]],
                            [/version\/([\w\.]+).+?(mobile\s?safari|safari)/i],
                            [g, h],
                            [/webkit.+?(gsa)\/([\w\.]+).+?(mobile\s?safari|safari)(\/[\w\.]+)/i],
                            [
                                [h, "GSA"], g
                            ],
                            [/webkit.+?(mobile\s?safari|safari)(\/[\w\.]+)/i],
                            [h, [g, x.str, C.browser.oldsafari.version]],
                            [/(webkit|khtml)\/([\w\.]+)/i],
                            [h, g],
                            [/(navigator|netscape)\/([\w\.-]+)/i],
                            [
                                [h, "Netscape"], g
                            ],
                            [/(swiftfox)/i, /(icedragon|iceweasel|camino|chimera|fennec|maemo\sbrowser|minimo|conkeror)[\/\s]?([\w\.\+]+)/i, /(firefox|seamonkey|k-meleon|icecat|iceape|firebird|phoenix|palemoon|basilisk|waterfox)\/([\w\.-]+)$/i, /(mozilla)\/([\w\.]+).+rv\:.+gecko\/\d+/i, /(polaris|lynx|dillo|icab|doris|amaya|w3m|netsurf|sleipnir)[\/\s]?([\w\.]+)/i, /(links)\s\(([\w\.]+)/i, /(gobrowser)\/?([\w\.]*)/i, /(ice\s?browser)\/v?([\w\._]+)/i, /(mosaic)[\/\s]([\w\.]+)/i],
                            [h, g]
                        ],
                        cpu: [
                            [/(?:(amd|x(?:(?:86|64)[_-])?|wow|win)64)[;\)]/i],
                            [
                                [b, "amd64"]
                            ],
                            [/(ia32(?=;))/i],
                            [
                                [b, O.lowerize]
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
                                [b, /ower/, "", O.lowerize]
                            ],
                            [/(sun4\w)[;\)]/i],
                            [
                                [b, "sparc"]
                            ],
                            [/((?:avr32|ia64(?=;))|68k(?=\))|arm(?:64|(?=v\d+[;l]))|(?=atmel\s)avr|(?:irix|mips|sparc)(?:64)?(?=;)|pa-risc)/i],
                            [
                                [b, O.lowerize]
                            ]
                        ],
                        device: [
                            [/\((ipad|playbook);[\w\s\),;-]+(rim|apple)/i],
                            [p, f, [m, y]],
                            [/applecoremedia\/[\w\.]+ \((ipad)/],
                            [p, [f, "Apple"],
                                [m, y]
                            ],
                            [/(apple\s{0,1}tv)/i],
                            [
                                [p, "Apple TV"],
                                [f, "Apple"]
                            ],
                            [/(archos)\s(gamepad2?)/i, /(hp).+(touchpad)/i, /(hp).+(tablet)/i, /(kindle)\/([\w\.]+)/i, /\s(nook)[\w\s]+build\/(\w+)/i, /(dell)\s(strea[kpr\s\d]*[\dko])/i],
                            [f, p, [m, y]],
                            [/(kf[A-z]+)\sbuild\/.+silk\//i],
                            [p, [f, "Amazon"],
                                [m, y]
                            ],
                            [/(sd|kf)[0349hijorstuw]+\sbuild\/.+silk\//i],
                            [
                                [p, x.str, C.device.amazon.model],
                                [f, "Amazon"],
                                [m, v]
                            ],
                            [/android.+aft([bms])\sbuild/i],
                            [p, [f, "Amazon"],
                                [m, T]
                            ],
                            [/\((ip[honed|\s\w*]+);.+(apple)/i],
                            [p, f, [m, v]],
                            [/\((ip[honed|\s\w*]+);/i],
                            [p, [f, "Apple"],
                                [m, v]
                            ],
                            [/(blackberry)[\s-]?(\w+)/i, /(blackberry|benq|palm(?=\-)|sonyericsson|acer|asus|dell|meizu|motorola|polytron)[\s_-]?([\w-]*)/i, /(hp)\s([\w\s]+\w)/i, /(asus)-?(\w+)/i],
                            [f, p, [m, v]],
                            [/\(bb10;\s(\w+)/i],
                            [p, [f, "BlackBerry"],
                                [m, v]
                            ],
                            [/android.+(transfo[prime\s]{4,10}\s\w+|eeepc|slider\s\w+|nexus 7|padfone|p00c)/i],
                            [p, [f, "Asus"],
                                [m, y]
                            ],
                            [/(sony)\s(tablet\s[ps])\sbuild\//i, /(sony)?(?:sgp.+)\sbuild\//i],
                            [
                                [f, "Sony"],
                                [p, "Xperia Tablet"],
                                [m, y]
                            ],
                            [/android.+\s([c-g]\d{4}|so[-l]\w+)(?=\sbuild\/|\).+chrome\/(?![1-6]{0,1}\d\.))/i],
                            [p, [f, "Sony"],
                                [m, v]
                            ],
                            [/\s(ouya)\s/i, /(nintendo)\s([wids3u]+)/i],
                            [f, p, [m, w]],
                            [/android.+;\s(shield)\sbuild/i],
                            [p, [f, "Nvidia"],
                                [m, w]
                            ],
                            [/(playstation\s[34portablevi]+)/i],
                            [p, [f, "Sony"],
                                [m, w]
                            ],
                            [/(sprint\s(\w+))/i],
                            [
                                [f, x.str, C.device.sprint.vendor],
                                [p, x.str, C.device.sprint.model],
                                [m, v]
                            ],
                            [/(htc)[;_\s-]+([\w\s]+(?=\)|\sbuild)|\w+)/i, /(zte)-(\w*)/i, /(alcatel|geeksphone|nexian|panasonic|(?=;\s)sony)[_\s-]?([\w-]*)/i],
                            [f, [p, /_/g, " "],
                                [m, v]
                            ],
                            [/(nexus\s9)/i],
                            [p, [f, "HTC"],
                                [m, y]
                            ],
                            [/d\/huawei([\w\s-]+)[;\)]/i, /(nexus\s6p)/i],
                            [p, [f, "Huawei"],
                                [m, v]
                            ],
                            [/(microsoft);\s(lumia[\s\w]+)/i],
                            [f, p, [m, v]],
                            [/[\s\(;](xbox(?:\sone)?)[\s\);]/i],
                            [p, [f, "Microsoft"],
                                [m, w]
                            ],
                            [/(kin\.[onetw]{3})/i],
                            [
                                [p, /\./g, " "],
                                [f, "Microsoft"],
                                [m, v]
                            ],
                            [/\s(milestone|droid(?:[2-4x]|\s(?:bionic|x2|pro|razr))?:?(\s4g)?)[\w\s]+build\//i, /mot[\s-]?(\w*)/i, /(XT\d{3,4}) build\//i, /(nexus\s6)/i],
                            [p, [f, "Motorola"],
                                [m, v]
                            ],
                            [/android.+\s(mz60\d|xoom[\s2]{0,2})\sbuild\//i],
                            [p, [f, "Motorola"],
                                [m, y]
                            ],
                            [/hbbtv\/\d+\.\d+\.\d+\s+\([\w\s]*;\s*(\w[^;]*);([^;]*)/i],
                            [
                                [f, O.trim],
                                [p, O.trim],
                                [m, T]
                            ],
                            [/hbbtv.+maple;(\d+)/i],
                            [
                                [p, /^/, "SmartTV"],
                                [f, "Samsung"],
                                [m, T]
                            ],
                            [/\(dtv[\);].+(aquos)/i],
                            [p, [f, "Sharp"],
                                [m, T]
                            ],
                            [/android.+((sch-i[89]0\d|shw-m380s|gt-p\d{4}|gt-n\d+|sgh-t8[56]9|nexus 10))/i, /((SM-T\w+))/i],
                            [
                                [f, "Samsung"], p, [m, y]
                            ],
                            [/smart-tv.+(samsung)/i],
                            [f, [m, T], p],
                            [/((s[cgp]h-\w+|gt-\w+|galaxy\snexus|sm-\w[\w\d]+))/i, /(sam[sung]*)[\s-]*(\w+-?[\w-]*)/i, /sec-((sgh\w+))/i],
                            [
                                [f, "Samsung"], p, [m, v]
                            ],
                            [/sie-(\w*)/i],
                            [p, [f, "Siemens"],
                                [m, v]
                            ],
                            [/(maemo|nokia).*(n900|lumia\s\d+)/i, /(nokia)[\s_-]?([\w-]*)/i],
                            [
                                [f, "Nokia"], p, [m, v]
                            ],
                            [/android[x\d\.\s;]+\s([ab][1-7]\-?[0178a]\d\d?)/i],
                            [p, [f, "Acer"],
                                [m, y]
                            ],
                            [/android.+([vl]k\-?\d{3})\s+build/i],
                            [p, [f, "LG"],
                                [m, y]
                            ],
                            [/android\s3\.[\s\w;-]{10}(lg?)-([06cv9]{3,4})/i],
                            [
                                [f, "LG"], p, [m, y]
                            ],
                            [/(lg) netcast\.tv/i],
                            [f, p, [m, T]],
                            [/(nexus\s[45])/i, /lg[e;\s\/-]+(\w*)/i, /android.+lg(\-?[\d\w]+)\s+build/i],
                            [p, [f, "LG"],
                                [m, v]
                            ],
                            [/(lenovo)\s?(s(?:5000|6000)(?:[\w-]+)|tab(?:[\s\w]+))/i],
                            [f, p, [m, y]],
                            [/android.+(ideatab[a-z0-9\-\s]+)/i],
                            [p, [f, "Lenovo"],
                                [m, y]
                            ],
                            [/(lenovo)[_\s-]?([\w-]+)/i],
                            [f, p, [m, v]],
                            [/linux;.+((jolla));/i],
                            [f, p, [m, v]],
                            [/((pebble))app\/[\d\.]+\s/i],
                            [f, p, [m, A]],
                            [/android.+;\s(oppo)\s?([\w\s]+)\sbuild/i],
                            [f, p, [m, v]],
                            [/crkey/i],
                            [
                                [p, "Chromecast"],
                                [f, "Google"]
                            ],
                            [/android.+;\s(glass)\s\d/i],
                            [p, [f, "Google"],
                                [m, A]
                            ],
                            [/android.+;\s(pixel c)[\s)]/i],
                            [p, [f, "Google"],
                                [m, y]
                            ],
                            [/android.+;\s(pixel( [23])?( xl)?)[\s)]/i],
                            [p, [f, "Google"],
                                [m, v]
                            ],
                            [/android.+;\s(\w+)\s+build\/hm\1/i, /android.+(hm[\s\-_]*note?[\s_]*(?:\d\w)?)\s+build/i, /android.+(mi[\s\-_]*(?:a\d|one|one[\s_]plus|note lte)?[\s_]*(?:\d?\w?)[\s_]*(?:plus)?)\s+build/i, /android.+(redmi[\s\-_]*(?:note)?(?:[\s_]*[\w\s]+))\s+build/i],
                            [
                                [p, /_/g, " "],
                                [f, "Xiaomi"],
                                [m, v]
                            ],
                            [/android.+(mi[\s\-_]*(?:pad)(?:[\s_]*[\w\s]+))\s+build/i],
                            [
                                [p, /_/g, " "],
                                [f, "Xiaomi"],
                                [m, y]
                            ],
                            [/android.+;\s(m[1-5]\snote)\sbuild/i],
                            [p, [f, "Meizu"],
                                [m, v]
                            ],
                            [/(mz)-([\w-]{2,})/i],
                            [
                                [f, "Meizu"], p, [m, v]
                            ],
                            [/android.+a000(1)\s+build/i, /android.+oneplus\s(a\d{4})\s+build/i],
                            [p, [f, "OnePlus"],
                                [m, v]
                            ],
                            [/android.+[;\/]\s*(RCT[\d\w]+)\s+build/i],
                            [p, [f, "RCA"],
                                [m, y]
                            ],
                            [/android.+[;\/\s]+(Venue[\d\s]{2,7})\s+build/i],
                            [p, [f, "Dell"],
                                [m, y]
                            ],
                            [/android.+[;\/]\s*(Q[T|M][\d\w]+)\s+build/i],
                            [p, [f, "Verizon"],
                                [m, y]
                            ],
                            [/android.+[;\/]\s+(Barnes[&\s]+Noble\s+|BN[RT])(V?.*)\s+build/i],
                            [
                                [f, "Barnes & Noble"], p, [m, y]
                            ],
                            [/android.+[;\/]\s+(TM\d{3}.*\b)\s+build/i],
                            [p, [f, "NuVision"],
                                [m, y]
                            ],
                            [/android.+;\s(k88)\sbuild/i],
                            [p, [f, "ZTE"],
                                [m, y]
                            ],
                            [/android.+[;\/]\s*(gen\d{3})\s+build.*49h/i],
                            [p, [f, "Swiss"],
                                [m, v]
                            ],
                            [/android.+[;\/]\s*(zur\d{3})\s+build/i],
                            [p, [f, "Swiss"],
                                [m, y]
                            ],
                            [/android.+[;\/]\s*((Zeki)?TB.*\b)\s+build/i],
                            [p, [f, "Zeki"],
                                [m, y]
                            ],
                            [/(android).+[;\/]\s+([YR]\d{2})\s+build/i, /android.+[;\/]\s+(Dragon[\-\s]+Touch\s+|DT)(\w{5})\sbuild/i],
                            [
                                [f, "Dragon Touch"], p, [m, y]
                            ],
                            [/android.+[;\/]\s*(NS-?\w{0,9})\sbuild/i],
                            [p, [f, "Insignia"],
                                [m, y]
                            ],
                            [/android.+[;\/]\s*((NX|Next)-?\w{0,9})\s+build/i],
                            [p, [f, "NextBook"],
                                [m, y]
                            ],
                            [/android.+[;\/]\s*(Xtreme\_)?(V(1[045]|2[015]|30|40|60|7[05]|90))\s+build/i],
                            [
                                [f, "Voice"], p, [m, v]
                            ],
                            [/android.+[;\/]\s*(LVTEL\-)?(V1[12])\s+build/i],
                            [
                                [f, "LvTel"], p, [m, v]
                            ],
                            [/android.+;\s(PH-1)\s/i],
                            [p, [f, "Essential"],
                                [m, v]
                            ],
                            [/android.+[;\/]\s*(V(100MD|700NA|7011|917G).*\b)\s+build/i],
                            [p, [f, "Envizen"],
                                [m, y]
                            ],
                            [/android.+[;\/]\s*(Le[\s\-]+Pan)[\s\-]+(\w{1,9})\s+build/i],
                            [f, p, [m, y]],
                            [/android.+[;\/]\s*(Trio[\s\-]*.*)\s+build/i],
                            [p, [f, "MachSpeed"],
                                [m, y]
                            ],
                            [/android.+[;\/]\s*(Trinity)[\-\s]*(T\d{3})\s+build/i],
                            [f, p, [m, y]],
                            [/android.+[;\/]\s*TU_(1491)\s+build/i],
                            [p, [f, "Rotor"],
                                [m, y]
                            ],
                            [/android.+(KS(.+))\s+build/i],
                            [p, [f, "Amazon"],
                                [m, y]
                            ],
                            [/android.+(Gigaset)[\s\-]+(Q\w{1,9})\s+build/i],
                            [f, p, [m, y]],
                            [/\s(tablet|tab)[;\/]/i, /\s(mobile)(?:[;\/]|\ssafari)/i],
                            [
                                [m, O.lowerize], f, p
                            ],
                            [/[\s\/\(](smart-?tv)[;\)]/i],
                            [
                                [m, T]
                            ],
                            [/(android[\w\.\s\-]{0,9});.+build/i],
                            [p, [f, "Generic"]]
                        ],
                        engine: [
                            [/windows.+\sedge\/([\w\.]+)/i],
                            [g, [h, "EdgeHTML"]],
                            [/webkit\/537\.36.+chrome\/(?!27)/i],
                            [
                                [h, "Blink"]
                            ],
                            [/(presto)\/([\w\.]+)/i, /(webkit|trident|netfront|netsurf|amaya|lynx|w3m|goanna)\/([\w\.]+)/i, /(khtml|tasman|links)[\/\s]\(?([\w\.]+)/i, /(icab)[\/\s]([23]\.[\d\.]+)/i],
                            [h, g],
                            [/rv\:([\w\.]{1,9}).+(gecko)/i],
                            [g, h]
                        ],
                        os: [
                            [/microsoft\s(windows)\s(vista|xp)/i],
                            [h, g],
                            [/(windows)\snt\s6\.2;\s(arm)/i, /(windows\sphone(?:\sos)*)[\s\/]?([\d\.\s\w]*)/i, /(windows\smobile|windows)[\s\/]?([ntce\d\.\s]+\w)/i],
                            [h, [g, x.str, C.os.windows.version]],
                            [/(win(?=3|9|n)|win\s9x\s)([nt\d\.]+)/i],
                            [
                                [h, "Windows"],
                                [g, x.str, C.os.windows.version]
                            ],
                            [/\((bb)(10);/i],
                            [
                                [h, "BlackBerry"], g
                            ],
                            [/(blackberry)\w*\/?([\w\.]*)/i, /(tizen)[\/\s]([\w\.]+)/i, /(android|webos|palm\sos|qnx|bada|rim\stablet\sos|meego|sailfish|contiki)[\/\s-]?([\w\.]*)/i],
                            [h, g],
                            [/(symbian\s?os|symbos|s60(?=;))[\/\s-]?([\w\.]*)/i],
                            [
                                [h, "Symbian"], g
                            ],
                            [/\((series40);/i],
                            [h],
                            [/mozilla.+\(mobile;.+gecko.+firefox/i],
                            [
                                [h, "Firefox OS"], g
                            ],
                            [/(nintendo|playstation)\s([wids34portablevu]+)/i, /(mint)[\/\s\(]?(\w*)/i, /(mageia|vectorlinux)[;\s]/i, /(joli|[kxln]?ubuntu|debian|suse|opensuse|gentoo|(?=\s)arch|slackware|fedora|mandriva|centos|pclinuxos|redhat|zenwalk|linpus)[\/\s-]?(?!chrom)([\w\.-]*)/i, /(hurd|linux)\s?([\w\.]*)/i, /(gnu)\s?([\w\.]*)/i],
                            [h, g],
                            [/(cros)\s[\w]+\s([\w\.]+\w)/i],
                            [
                                [h, "Chromium OS"], g
                            ],
                            [/(sunos)\s?([\w\.\d]*)/i],
                            [
                                [h, "Solaris"], g
                            ],
                            [/\s([frentopc-]{0,4}bsd|dragonfly)\s?([\w\.]*)/i],
                            [h, g],
                            [/(haiku)\s(\w+)/i],
                            [h, g],
                            [/cfnetwork\/.+darwin/i, /ip[honead]{2,4}(?:.*os\s([\w]+)\slike\smac|;\sopera)/i],
                            [
                                [g, /_/g, "."],
                                [h, "iOS"]
                            ],
                            [/(mac\sos\sx)\s?([\w\s\.]*)/i, /(macintosh|mac(?=_powerpc)\s)/i],
                            [
                                [h, "Mac OS"],
                                [g, /_/g, "."]
                            ],
                            [/((?:open)?solaris)[\/\s-]?([\w\.]*)/i, /(aix)\s((\d)(?=\.|\)|\s)[\w\.])*/i, /(plan\s9|minix|beos|os\/2|amigaos|morphos|risc\sos|openvms|fuchsia)/i, /(unix)\s?([\w\.]*)/i],
                            [h, g]
                        ]
                    },
                    k = function (e, t) {
                        if ("object" == typeof e && (t = e, e = o), !(this instanceof k)) return new k(e, t).getResult();
                        var n = e || (r && r.navigator && r.navigator.userAgent ? r.navigator.userAgent : a),
                            i = t ? O.extend(S, t) : S;
                        return this.getBrowser = function () {
                            var e = {
                                name: o,
                                version: o
                            };
                            return x.rgx.call(e, n, i.browser), e.major = O.major(e.version), e
                        }, this.getCPU = function () {
                            var e = {
                                architecture: o
                            };
                            return x.rgx.call(e, n, i.cpu), e
                        }, this.getDevice = function () {
                            var e = {
                                vendor: o,
                                model: o,
                                type: o
                            };
                            return x.rgx.call(e, n, i.device), e
                        }, this.getEngine = function () {
                            var e = {
                                name: o,
                                version: o
                            };
                            return x.rgx.call(e, n, i.engine), e
                        }, this.getOS = function () {
                            var e = {
                                name: o,
                                version: o
                            };
                            return x.rgx.call(e, n, i.os), e
                        }, this.getResult = function () {
                            return {
                                ua: this.getUA(),
                                browser: this.getBrowser(),
                                engine: this.getEngine(),
                                os: this.getOS(),
                                device: this.getDevice(),
                                cpu: this.getCPU()
                            }
                        }, this.getUA = function () {
                            return n
                        }, this.setUA = function (e) {
                            return n = e, this
                        }, this
                    };
                k.VERSION = "0.7.20", k.BROWSER = {
                    NAME: h,
                    MAJOR: "major",
                    VERSION: g
                }, k.CPU = {
                    ARCHITECTURE: b
                }, k.DEVICE = {
                    MODEL: p,
                    VENDOR: f,
                    TYPE: m,
                    CONSOLE: w,
                    MOBILE: v,
                    SMARTTV: T,
                    TABLET: y,
                    WEARABLE: A,
                    EMBEDDED: "embedded"
                }, k.ENGINE = {
                    NAME: h,
                    VERSION: g
                }, k.OS = {
                    NAME: h,
                    VERSION: g
                }, typeof t !== c ? (typeof e !== c && e.exports && (t = e.exports = k), t.UAParser = k) : 1 ? (i = function () {
                    return k
                }.call(t, n, t, e)) === o || (e.exports = i) : r && (r.UAParser = k);
                var E = r && (r.jQuery || r.Zepto);
                if (typeof E !== c && !E.ua) {
                    var L = new k;
                    E.ua = L.getResult(), E.ua.get = function () {
                        return L.getUA()
                    }, E.ua.set = function (e) {
                        L.setUA(e);
                        var t = L.getResult();
                        for (var n in t) E.ua[n] = t[n]
                    }
                }
            }("object" == typeof window ? window : this)
        },
        99: function (e, t, n) {
            "use strict";
            e.exports = {
                isProduction: "production" == "production",
                isStaging: "production" == "staging",
                isTest: "production" == "test"
            }
        }
    }, [420])
});
//# sourceMappingURL=record.js.map

function popupOpen(id) {
    fadeIn(document.getElementById(id), 300);
}
function popupClose(id) {

    fadeOut(document.getElementById(id), 300);
}


function fadeIn(elem, ms) {
    if (!elem)
        return;

    elem.style.opacity = 0;
    elem.style.filter = "alpha(opacity=0)";
    elem.style.display = "inline-block";
    elem.style.visibility = "visible";

    if (ms) {
        var opacity = 0;
        var timer = setInterval(function () {
            opacity += 50 / ms;
            if (opacity >= 1) {
                clearInterval(timer);
                opacity = 1;
            }
            elem.style.opacity = opacity;
            elem.style.filter = "alpha(opacity=" + opacity * 100 + ")";
        }, 50);
    }
    else {
        elem.style.opacity = 1;
        elem.style.filter = "alpha(opacity=1)";
    }
}

function fadeOut(elem, ms) {
    if (!elem)
        return;

    if (ms) {
        var opacity = 1;
        var timer = setInterval(function () {
            opacity -= 50 / ms;
            if (opacity <= 0) {
                clearInterval(timer);
                opacity = 0;
                elem.style.display = "none";
                elem.style.visibility = "hidden";
            }
            elem.style.opacity = opacity;
            elem.style.filter = "alpha(opacity=" + opacity * 100 + ")";
        }, 50);
    }
    else {
        elem.style.opacity = 0;
        elem.style.filter = "alpha(opacity=0)";
        elem.style.display = "none";
        elem.style.visibility = "hidden";
    }
}
function tacPreprocess(target) {
    if (target.includes("d-XPath")) return "auto-located-by-tac";
    return target;
}
function getElementByXpath(path) {
    console.log(document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null));
    return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
}



function dragElement(elmnt) {
    var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
    if (document.getElementById(elmnt.id)) {
        /* if present, the header is where you move the DIV from:*/
        document.getElementById(elmnt.id).onmousedown = dragMouseDown;
    } else {
        /* otherwise, move the DIV from anywhere inside the DIV:*/
        elmnt.onmousedown = dragMouseDown;
    }

    function dragMouseDown(e) {
        e = e || window.event;
        e.preventDefault();
        // get the mouse cursor position at startup:
        pos3 = e.clientX;
        pos4 = e.clientY;
        document.onmouseup = closeDragElement;
        // call a function whenever the cursor moves:
        document.onmousemove = elementDrag;
    }

    function elementDrag(e) {
        e = e || window.event;
        e.preventDefault();
        // calculate the new cursor position:
        pos1 = pos3 - e.clientX;
        pos2 = pos4 - e.clientY;
        pos3 = e.clientX;
        pos4 = e.clientY;
        // set the element's new position:
        elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
        elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
    }

    function closeDragElement() {
        /* stop moving when mouse button is released:*/
        document.onmouseup = null;
        document.onmousemove = null;
    }
}
function generateUniqNumber(){
    return random = random+1;
}
function cobotPopup(pType, e, inputVal, r) {

    var iDiv = document.createElement('div');
    iDiv.id = 'cobot_overlay';
    iDiv.style.position = "fixed";
    iDiv.style.height = "100%";
    iDiv.style.width = "100%";
    iDiv.style.top = "0";
    iDiv.style.right = "0";
    iDiv.style.bottom = "0";
    iDiv.style.left = "0";
    iDiv.style.zIndex = "99999999";
    iDiv.style.background = "rgba(0,0,0,0.8)";
    iDiv.style.display = "none";

    var _iDiv = document.createElement('div');
    _iDiv.id = 'popup';
    //_iDiv.style.maxWidth  = "350px";
    _iDiv.style.width = "25%";

    // _iDiv.style.height = "80%";
    _iDiv.style.padding = "12px";
    _iDiv.style.position = "relative";
    _iDiv.style.background = "#fff";
    _iDiv.style.margin = "0px auto";

    var __iDiv = document.createElement('div');
    var t2 = document.createElement("p");
    t2.innerHTML = "X";
    t2.style.position = "absolute";
    t2.style.right = "11px";
    t2.style.top = "5px";
    __iDiv.appendChild(t2);
    __iDiv.id = 'close';
    _iDiv.style.position = "absolute";
    _iDiv.style.top = "10px";
    if (pType == 'validate') {
        //_iDiv.style.maxHeight = "30%";
    } else {
        _iDiv.style.right = "10px";
        //_iDiv.style.maxHeight = "40%";
    }

    _iDiv.style.cursor = "pointer";
    _iDiv.style.color = "#000";
    __iDiv.onclick = function () {
        popupClose("cobot_overlay");
        window.document.body.removeChild(iDiv)
    };
    var h = document.createElement("H1");
    var t = document.createElement("p");
    if (pType == 'validate') {

        t.innerHTML = "Please Enter Variable For Text Validation";
        t.style.color = "#2196f3";
    } else {
        t.innerHTML = "Please Enter Field Name";
        t.style.color = "#c60101";
    }

    t.style.fontSize = "14px";
    h.appendChild(t);
    var p = document.createElement("p");
    p.appendChild(t);
    _iDiv.appendChild(__iDiv);
    // _iDiv.appendChild(h);
    var x = document.createElement("INPUT");
    x.id = 'cobot_input_dynamic_parameter';
    x.setAttribute("autocomplete", "off");
    if (pType != 'validate') {
        x.setAttribute("list", "ssm_default_var_select");
    }

    x.autofocus = true;
    x.onfocus = function () {
        this.select();
    }
    x.onkeyup = function (e) {
        let element = document.getElementById("ssm_default_var_select");
        if (element != null)
            element.value = "select";
        if (e.target.value != "") {
            btn.style.visibility = "visible";
        } else {
            btn.style.visibility = "hidden";
        }
    };
    x.setAttribute("type", "text");
    if (pType == 'validate') {
        x.setAttribute("value", "");
    } else {
        //x.setAttribute("value", e.target[0][0]);
        x.setAttribute("value", "");
    }

    x.style.marginRight = "10px";
    x.style.height = "30px";
    x.style.borderRadius = "4px";
    x.style.border = "1px solid #ccc";
    x.style.width = "90%";
    var btn = document.createElement("BUTTON");
    if (pType == 'validate') {
        btn.id = 'textvalidator_cobot_div';
    } else {
        btn.id = 'cobot_input_button_for_dynamic_parameter';
    }

    btn.innerHTML = "SAVE";
    btn.style.padding = "5px 20px";
    if (pType == 'validate') {
        btn.style.background = "#2196f3";
        btn.style.visibility = "hidden";
    } else {
        btn.style.background = "#c60101";
        btn.style.visibility = "hidden";
    }

    //btn.style.border = "1px solid #336699"; 
    btn.style.marginTop = "8px";
    btn.style.color = "#fff";

    btn.onclick = function () {
        var rqObj = {};
        if (pType == 'validate') {
            var key = document.getElementById("cobot_input_dynamic_parameter").value;
            if (key != "") {
                btn.style.visibility = "visible";
                if (sessionStorage.getItem("valid") == null) {
                    var valid = {};
                    valid[tacPreprocess(key)] = document.getElementById("cobot_input_dynamic_parameter").value;
                    sessionStorage.setItem("valid", JSON.stringify(valid));
                } else {
                    var valid = JSON.parse(sessionStorage.getItem("valid"));
                    delete valid[key];
                    valid[tacPreprocess(key)] = document.getElementById("cobot_input_dynamic_parameter").value;
                    sessionStorage.setItem("valid", JSON.stringify(valid));
                }
            }
            rqObj.search = JSON.parse(sessionStorage.getItem("valid"));
        } else {

            var shownVal = document.getElementById("cobot_input_dynamic_parameter").value;
            // var value2send = null;
            // try{
            //     value2send = document.querySelector("#ssm_default_var_select option[value='" + shownVal + "']").dataset.value;
            // }catch(e){
            //     value2send = "${"+shownVal+"}";
            // }
            //var value2send = document.querySelector("#ssm_default_var_select option[value='" + shownVal + "']").dataset.value;
            if (String(e.value) != "" || inputVal) {
                // map.set(tacPreprocess(document.getElementById("cobot_input_dynamic_parameter").value), e.value);
                //   hash[tacPreprocess(document.getElementById("cobot_input_dynamic_parameter").value)] = e.value;
                // hash[tacPreprocess(e.target[0][0])] = document.getElementById("cobot_input_dynamic_parameter").value; 
                //localStorage.clear();
                if (sessionStorage.getItem("hash") == null) {
                    var hash = {};
                    //hash[tacPreprocess(e.target[0][0])] = value2send;
                    hash[tacPreprocess(e.target[0][0])] = "${"+shownVal+"}";
                    sessionStorage.setItem("hash", JSON.stringify(hash));
                } else {
                    var hash = JSON.parse(sessionStorage.getItem("hash"));
                    delete hash[e.target[0][0]];
                    //hash[tacPreprocess(e.target[0][0])] = value2send;
                    hash[tacPreprocess(e.target[0][0])] = "${"+shownVal+"}";
                    sessionStorage.setItem("hash", JSON.stringify(hash));
                }
            }
            rqObj.data = JSON.parse(sessionStorage.getItem("hash"));
        }

        // let obj = Array.from(hash).reduce((obj, [key, value]) => (
        //     Object.assign(obj, { [key]: value }) // Be careful! Maps can have non-String keys; object literals can't.
        //   ), {});



        rqObj.flag = "input";
        rqObj.url = COBOT_URL;
        r.a.runtime.sendMessage(rqObj);
        console.log(rqObj);
        console.log(hash);
        popupClose("cobot_overlay");
        window.document.body.removeChild(iDiv)
    };

    var ssmVarSelect = document.createElement("select");
    //var ssmVarSelect = document.createElement("datalist");
    ssmVarSelect.setAttribute("id", "ssm_default_var_select");
    ssmVarSelect.style.marginTop = "10px";
    ssmVarSelect.style.height = "28px";
    ssmVarSelect.style.width = "92%";
    ssmVarSelect.onchange = function(event){
        if(event.target.value!="select"){
            btn.style.visibility = "visible";
            document.getElementById("cobot_input_dynamic_parameter").value = event.target.value.replace("$", "").replace("{", "").replace("}", "");
        }

    };
    var option = document.createElement("option");
            option.text = "Please select SSM variables";
            option.value = "select";
            ssmVarSelect.appendChild(option);
    for (var key in popupParams) {

        var optgroup1 = document.createElement("optgroup");
        optgroup1.label = key;
        for (var k in popupParams[key]) {
            var option1 = document.createElement("option");
            option1.text = k;
            //option1.setAttribute("value", k);
            //option1.setAttribute("data-value", popupParams[key][k]);
            option1.value = popupParams[key][k];
            optgroup1.appendChild(option1);
        }
        ssmVarSelect.appendChild(optgroup1);
    }

    var newLinveDive = document.createElement("div");
    newLinveDive.appendChild(btn);
    _iDiv.appendChild(p);
    _iDiv.appendChild(x);
    if (pType == 'validate') {
        var parentDiv = document.getElementById("ssm_default_var_select");
        if (parentDiv != null) {
            _iDiv.removeChild(ssmVarSelect)
        }

    } else {
        _iDiv.appendChild(ssmVarSelect);
    }
    _iDiv.appendChild(newLinveDive);

    iDiv.appendChild(_iDiv);
    
   if(enableCustomPopup){
    window.document.body.appendChild(iDiv)
    popupOpen("cobot_overlay");
   }
    

   /* new testbot code  */
   if(!enableCustomPopup){
    var rqObj = {};
    if (pType == 'validate') {
        var key = e.value;
        if (key != "") {
            btn.style.visibility = "visible";
            if (sessionStorage.getItem("valid") == null) {
                var valid = {};
                valid[tacPreprocess(key)] = e.value;
                sessionStorage.setItem("valid", JSON.stringify(valid));
            } else {
                var valid = JSON.parse(sessionStorage.getItem("valid"));
                delete valid[key];
                valid[tacPreprocess(key)] = e.value;
                sessionStorage.setItem("valid", JSON.stringify(valid));
            }
        }
        rqObj.search = JSON.parse(sessionStorage.getItem("valid"));
    } else {
        var shownVal = e.value;
          if (String(e.value) != "" || inputVal) {
            // uniq key values pair  
            if (sessionStorage.getItem("hash") == null) {
            hash[generateUniqNumber()] = shownVal;
            sessionStorage.setItem("hash", JSON.stringify(hash));
            }else{
               
                hash = JSON.parse(sessionStorage.getItem("hash"));
                random =   Object.keys(hash).length;
                hash[generateUniqNumber()] = shownVal;
                 sessionStorage.setItem("hash", JSON.stringify(hash));
            }
              /*
            if (sessionStorage.getItem("hash") == null) {
                var hash = {};
                hash[tacPreprocess(e.target[0][0])] = shownVal;
                sessionStorage.setItem("hash", JSON.stringify(hash));
            } else {
                var hash = JSON.parse(sessionStorage.getItem("hash"));
                delete hash[e.target[0][0]];
                hash[tacPreprocess(e.target[0][0])] = shownVal;
                sessionStorage.setItem("hash", JSON.stringify(hash));
            }*/
        }
        rqObj.data = JSON.parse(sessionStorage.getItem("hash"));
    }
    rqObj.flag = "input";
    rqObj.url = COBOT_URL;
    r.a.runtime.sendMessage(rqObj);
   }

}
