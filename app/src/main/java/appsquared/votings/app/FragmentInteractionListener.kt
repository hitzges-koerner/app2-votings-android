package appsquared.votings.app


interface FragmentInteractionListener {
    fun fragmentInteraction(action: Int, javaClass: Class<*>)
    fun appThemeChanger(theme: String)
}
