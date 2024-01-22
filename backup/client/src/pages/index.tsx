import {infoScreenState} from "@atoms/InfoScreenAtom";
import {showSearchResultsState} from "@atoms/SearchResultScreen";
import Footer from "@components/footer/Footer";
import Header from "@components/header/Header";
import SearchResultScreen from "@components/header/search/SearchResultScreen";
import HomeScreen from "@components/HomeScreen";
import InfoScreen from "@components/infoScreen/InfoScreen";

import {useRecoilValue} from "recoil";

export default function Home() {
    const showInfoScreen = useRecoilValue(infoScreenState)
    const showSearchResults = useRecoilValue(showSearchResultsState)

    return (<>
        <div className={`relative h-[100%] bg-[#1a1920] z-0 ${showInfoScreen ? '!h-screen overflow-hidden' : ""}`}>
            {/*  HEADER  */}
            <Header/>

            {/*  MAIN  */}
            <main className="lg:space-t-24 h-fit">
                {showSearchResults && <SearchResultScreen/>}
                {showInfoScreen && <InfoScreen/>}
                <HomeScreen className={showSearchResults ? "hidden" : "visible"}/>
            </main>

            {/*  FOOTER  */}
            <Footer/>
        </div>
    </>)
}
